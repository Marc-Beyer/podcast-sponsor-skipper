package de.devbeyer.podcast_sponsorskipper.ui.navigation

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.media.session.PlaybackState
import android.net.Uri
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
import androidx.media3.session.SessionToken
import androidx.work.WorkManager
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.EpisodeUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import de.devbeyer.podcast_sponsorskipper.service.PlaybackService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val podcastsUseCases: PodcastsUseCases,
    private val episodeUseCases: EpisodeUseCases,
    private val workManager: WorkManager,
    private val application: Application,
) : ViewModel() {
    private val _state = mutableStateOf(NavigationState())
    val state: State<NavigationState> = _state

    private var updatePositionJob: Job? = null

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
        }

    }

    init {
        initializeMediaController(context = application)
    }
    private fun setMediaController(mediaController: MediaController?){
        _state.value = state.value.copy(
            mediaController = mediaController,
        )
    }

    private fun setIsPlaying(isPlaying:Boolean){
        if (isPlaying) startUpdatingPosition() else stopUpdatingPosition()
        _state.value = state.value.copy(
            isPlaying = isPlaying,
        )
    }

    private fun setCurrentEpisodeAndPodcast(episode: Episode, podcast: PodcastWithRelations){
        _state.value = state.value.copy(
            selectedEpisode = episode,
            selectedPodcast = podcast,
        )
    }

    private fun updateCurrentPosition(){
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
                delay(1000) // Update every second
            }
        }
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
                        when(playbackState){
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