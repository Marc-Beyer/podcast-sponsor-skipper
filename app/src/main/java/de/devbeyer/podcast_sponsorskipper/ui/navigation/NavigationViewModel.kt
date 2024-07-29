package de.devbeyer.podcast_sponsorskipper.ui.navigation

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.media.session.PlaybackState
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import androidx.media3.session.SessionToken
import androidx.work.WorkManager
import androidx.work.await
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.EpisodeUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.user.UserUseCases
import de.devbeyer.podcast_sponsorskipper.service.PlaybackService
import de.devbeyer.podcast_sponsorskipper.util.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max


@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val podcastsUseCases: PodcastsUseCases,
    private val episodeUseCases: EpisodeUseCases,
    private val userUseCases: UserUseCases,
    private val workManager: WorkManager,
    private val application: Application,
) : ViewModel() {
    private val _state = mutableStateOf(NavigationState())
    val state: State<NavigationState> = _state

    private var updatePositionJob: Job? = null

    init {
        initializeMediaController(context = application)
    }

    fun onEvent(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.PlayEpisode -> {
                Log.i("AAA", "Play ${event.episode.title}")
                val episode = event.episode
                val podcast = event.podcast
                setCurrentEpisodeAndPodcast(event.episode, event.podcast)

                val mediaItem =
                    MediaItem.Builder()
                        .setMediaId("media-1")
                        .setUri(episode.episodePath)
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setArtist(podcast.podcast.title)
                                .setTitle(episode.title)
                                .setArtworkUri(
                                    Uri.parse(
                                        episode.imagePath ?: episode.imageUrl
                                    )
                                )
                                .build()
                        )
                        .build()

                state.value.mediaController?.setMediaItem(mediaItem)
                state.value.mediaController?.prepare()
                state.value.mediaController?.play()

            }

            is NavigationEvent.SeekTo -> {
                state.value.mediaController?.seekTo(event.position)
            }

            is NavigationEvent.SkipBack -> {
                state.value.mediaController?.seekBack()
            }

            is NavigationEvent.SkipForward -> {
                state.value.mediaController?.seekForward()
            }

            is NavigationEvent.Play -> {
                state.value.mediaController?.play()
            }

            is NavigationEvent.Stop -> {
                state.value.mediaController?.stop()
            }

            is NavigationEvent.Close -> {
                state.value.mediaController?.stop()
                _state.value = state.value.copy(
                    selectedEpisode = null,
                    selectedPodcast = null,
                    isPlaying = false,
                    sponsorSectionStart = null,
                    sponsorSectionEnd = null,
                    isPreviewing = PreviewState.NONE,
                )
            }

            is NavigationEvent.Unsubscribe -> {
                viewModelScope.launch {
                    event.podcastWithRelations?.let {
                        podcastsUseCases.deleteLocalPodcastUseCase(it.podcast)
                    }
                }
            }

            is NavigationEvent.StartSponsorSection -> {
                state.value.mediaController?.let { mediaController ->
                    _state.value = state.value.copy(
                        sponsorSectionStart = mediaController.currentPosition,
                        duration = mediaController.duration,
                    )
                }
            }

            is NavigationEvent.EndSponsorSection -> {
                state.value.mediaController?.let { mediaController ->
                    setIsPreviewing(PreviewState.NONE)
                    val sponsorSectionEnd = mediaController.currentPosition
                    _state.value = state.value.copy(
                        sponsorSectionEnd = sponsorSectionEnd,
                        duration = mediaController.duration,
                    )
                    state.value.sponsorSectionStart?.let {
                        mediaController.stop()
                        schedulePlaybackAction(
                            startPositionMs = it,
                            endPositionMs = sponsorSectionEnd
                        )
                    }
                }
            }

            is NavigationEvent.Preview -> {
                state.value.mediaController?.let { mediaController ->
                    state.value.sponsorSectionStart?.let {
                        mediaController.seekTo(max(it - Constants.PREVIEW_LEAD_TIME, 0))
                        setIsPreviewing(PreviewState.PREVIEWING)
                        mediaController.play()
                    }
                }
            }

            NavigationEvent.SubmitSegment -> {
                viewModelScope.launch {
                    val episodeUrl = state.value.selectedEpisode?.episodeUrl
                    val podcastUrl = state.value.selectedPodcast?.podcast?.url
                    val startPosition = state.value.sponsorSectionStart
                    val endPosition = state.value.sponsorSectionEnd

                    if (
                        episodeUrl != null &&
                        podcastUrl != null &&
                        startPosition != null &&
                        endPosition != null
                    ) {
                        Log.i("AAA", "submit SponsorSection $episodeUrl $podcastUrl $startPosition $endPosition")
                        podcastsUseCases.submitSponsorSectionUseCase(
                            episodeUrl = episodeUrl,
                            podcastUrl = podcastUrl,
                            startPosition = startPosition,
                            endPosition = endPosition,
                        ).firstOrNull()
                        _state.value = state.value.copy(
                            selectedEpisode = null,
                            selectedPodcast = null,
                            sponsorSectionStart = null,
                            sponsorSectionEnd = null,
                            isPreviewing = PreviewState.NONE,
                        )
                    }
                }
            }
        }

    }

    private fun schedulePlaybackAction(startPositionMs: Long, endPositionMs: Long) {
        viewModelScope.launch {
            val command = SessionCommand(Constants.COMMAND_SCHEDULE_EVENT, Bundle.EMPTY)
            val args = Bundle().apply {
                putLong("START_POSITION_MS", startPositionMs)
                putLong("END_POSITION_MS", endPositionMs)
            }
            Log.i("AAA", "sendCustomCommand to mediaController!!!!!")
            val result = state.value.mediaController?.sendCustomCommand(command, args)?.await()
            Log.i("AAA", "result $result")
            if (result != null && result.resultCode == SessionResult.RESULT_SUCCESS) {
                Log.i("AAA", "SessionResult.RESULT_SUCCESS")
            } else {
                Log.i("AAA", "SessionResult.RESULT_failure")
            }
        }
    }

    private fun setMediaController(mediaController: MediaController?) {
        _state.value = state.value.copy(
            mediaController = mediaController,
        )
    }

    private fun setIsPlaying(isPlaying: Boolean) {
        if (isPlaying) startUpdatingPosition() else stopUpdatingPosition()
        _state.value = state.value.copy(
            isPlaying = isPlaying,
        )
    }

    private fun setCurrentEpisodeAndPodcast(episode: Episode, podcast: PodcastWithRelations) {
        _state.value = state.value.copy(
            selectedEpisode = episode,
            selectedPodcast = podcast,
        )
    }

    private fun updateCurrentPosition() {
        state.value.mediaController?.let {
            _state.value = state.value.copy(
                currentPosition = it.currentPosition,
            )
        }
    }

    private fun updateDuration() {
        state.value.mediaController?.let {
            _state.value = state.value.copy(
                duration = it.duration,
            )
        }
    }

    private fun startUpdatingPosition() {
        updatePositionJob?.cancel()
        updatePositionJob = viewModelScope.launch {
            while (true) {
                updateCurrentPosition()
                if (state.value.isPreviewing == PreviewState.PREVIEWING) {
                    if (
                        state.value.currentPosition >= (state.value.sponsorSectionEnd ?: 0)
                        + Constants.PREVIEW_POST_TIME
                    ) {
                        setIsPreviewing(PreviewState.FINISHED)
                        state.value.mediaController?.stop()
                    }
                }
                delay(1000)
            }
        }
    }

    private fun setIsPreviewing(previewState: PreviewState) {
        _state.value = state.value.copy(
            isPreviewing = previewState,
        )
    }

    private fun stopUpdatingPosition() {
        updatePositionJob?.cancel()
    }

    private fun initializeMediaController(context: Context) {
        val sessionToken =
            SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                setMediaController(controllerFuture.get())
                state.value.mediaController?.addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        Log.i("AAA", "isPlaying changed to $isPlaying")
                        setIsPlaying(isPlaying)
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        Log.i("AAA", "playbackState changed to $playbackState")
                        updateCurrentPosition()
                        when (playbackState) {
                            PlaybackState.STATE_NONE -> setIsPlaying(false)
                            PlaybackState.STATE_STOPPED -> setIsPlaying(false)
                            PlaybackState.STATE_PAUSED -> setIsPlaying(false)
                            PlaybackState.STATE_PLAYING -> setIsPlaying(true)
                        }
                    }

                    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                        updateDuration()
                    }

                    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                        updateDuration()
                    }

                    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                        updateCurrentPosition()
                    }

                    override fun onPositionDiscontinuity(reason: Int) {
                        updateCurrentPosition()
                    }

                    override fun onPositionDiscontinuity(
                        oldPosition: Player.PositionInfo,
                        newPosition: Player.PositionInfo,
                        reason: Int
                    ) {
                        updateCurrentPosition()
                    }
                })
            },
            MoreExecutors.directExecutor()
        )
    }
}