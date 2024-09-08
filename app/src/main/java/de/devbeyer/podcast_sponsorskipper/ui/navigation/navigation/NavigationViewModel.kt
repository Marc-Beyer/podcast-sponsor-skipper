package de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionToken
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.data.worker.UpdateManager
import de.devbeyer.podcast_sponsorskipper.data.worker.UpdateWorker
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.models.db.SponsorSection
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.EpisodeUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.settings.SettingsUseCases
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
    private val settingsUseCases: SettingsUseCases,
    private val workManager: WorkManager,
    private val application: Application,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _state = mutableStateOf(NavigationState())
    val state: State<NavigationState> = _state

    private var updatePositionJob: Job? = null

    init {
        initializeMediaController(context = application)
        viewModelScope.launch {
            UpdateManager.activeUpdateUrlsFlow.collect { activeUrls ->
                _state.value = state.value.copy(activeUpdateUrls = activeUrls)
            }
        }
        getSettings()
    }

    fun onEvent(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.ChangeBooleanSettings -> {
                viewModelScope.launch {
                    settingsUseCases.setBooleanSettingUseCase(
                        settingKey = event.settingKey,
                        value = event.value,
                    )
                    getSettings()
                }
            }

            is NavigationEvent.ChangeIntSettings -> {
                viewModelScope.launch {
                    settingsUseCases.setIntSettingUseCase(
                        settingKey = event.settingKey,
                        value = event.value,
                    )
                    getSettings()
                }
            }

            is NavigationEvent.SetNavigateUp -> {
                _state.value = state.value.copy(
                    navigateUp = event.navigateUp,
                )
            }

            is NavigationEvent.UpdateSettings -> {
                getSettings()
            }

            is NavigationEvent.ChangeCurNavEpisode -> {
                _state.value = state.value.copy(
                    currentNavEpisode = event.episode,
                )
            }

            is NavigationEvent.ChangeCurNavPodcast -> {
                _state.value = state.value.copy(
                    currentNavPodcast = event.podcastWithRelations,
                )
            }

            is NavigationEvent.UpdatePodcast -> {
                val workData = workDataOf(
                    "url" to event.podcast.url,
                    "title" to event.podcast.title,
                )
                val updateWorkRequest = OneTimeWorkRequestBuilder<UpdateWorker>()
                    .setInputData(workData)
                    .build()
                workManager.enqueue(updateWorkRequest)
            }

            is NavigationEvent.UpdatePodcasts -> {
                viewModelScope.launch {
                    podcastsUseCases.getLocalPodcastsUseCase().firstOrNull()?.let { podcasts ->
                        podcasts.forEach {
                            val workData = workDataOf(
                                "url" to it.podcast.url,
                                "title" to it.podcast.title,
                            )
                            val updateWorkRequest = OneTimeWorkRequestBuilder<UpdateWorker>()
                                .setInputData(workData)
                                .build()
                            workManager.enqueue(updateWorkRequest)
                        }
                    }
                }
            }

            is NavigationEvent.PlayEpisode -> {
                val episode = event.episode
                val podcast = event.podcast
                setCurrentEpisodeAndPodcast(event.episode, event.podcast)
                loadSponsorSections(episodeUrl = episode.episodeUrl)
                setEpisodeAsMediaItem(episode, podcast)
            }

            is NavigationEvent.SeekTo -> {
                state.value.mediaController?.seekTo(event.position)
            }

            is NavigationEvent.SkipBack -> {
                val rewindTime = state.value.settings.rewindTime
                if (rewindTime > 0) {
                    state.value.mediaController?.let {
                        it.seekTo(it.currentPosition - rewindTime * 1000)
                    }
                } else {
                    state.value.mediaController?.seekBack()
                }
            }

            is NavigationEvent.SkipForward -> {
                val forwardTime = state.value.settings.forwardTime
                if (forwardTime > 0) {
                    state.value.mediaController?.let {
                        it.seekTo(it.currentPosition + forwardTime * 1000)
                    }
                } else {
                    state.value.mediaController?.seekForward()
                }
            }

            is NavigationEvent.OpenChangePlaybackSpeed -> {
                _state.value = state.value.copy(
                    isSpeedOpen = !state.value.isSpeedOpen,
                )
            }

            is NavigationEvent.ChangePlaybackSpeed -> {
                _state.value.mediaController?.setPlaybackSpeed(event.value)
                _state.value = state.value.copy(
                    isSpeedOpen = !state.value.isSpeedOpen,
                    currentPlaybackSpeed = event.value,
                )
            }

            is NavigationEvent.Play -> {
                state.value.mediaController?.play()
            }

            is NavigationEvent.Stop -> {
                state.value.mediaController?.stop()
            }

            is NavigationEvent.Close -> {
                closePodcastEpisode()
            }

            is NavigationEvent.Unsubscribe -> {
                _state.value = state.value.copy(
                    selectedPodcastForUnsubscribe = event.podcastWithRelations?.podcast,
                )
            }

            is NavigationEvent.ConfirmUnsubscribe -> {
                viewModelScope.launch {
                    state.value.selectedPodcastForUnsubscribe?.let {
                        podcastsUseCases.deleteLocalPodcastUseCase(it)
                    }
                    _state.value = state.value.copy(
                        selectedPodcastForUnsubscribe = null,
                    )
                    state.value.navigateUp()
                }
            }

            is NavigationEvent.Favorite -> {
                viewModelScope.launch {
                    episodeUseCases.favoriteEpisodeUseCase(
                        episode = event.episode,
                        favorite = event.favorite,
                    )
                    _state.value = state.value.copy(
                        currentNavEpisode = event.episode.copy(
                            favorite = event.favorite,
                        ),
                    )
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
                    state.value.sponsorSectionStart?.let {
                        var startPositionMs = it
                        var endPositionMs = mediaController.currentPosition
                        if (startPositionMs > endPositionMs) {
                            val tmp = startPositionMs
                            startPositionMs = endPositionMs
                            endPositionMs = tmp
                        }
                        _state.value = state.value.copy(
                            sponsorSectionStart = startPositionMs,
                            sponsorSectionEnd = endPositionMs,
                            duration = mediaController.duration,
                        )
                        mediaController.stop()
                        schedulePlaybackAction(
                            startPositionMs = startPositionMs,
                            endPositionMs = endPositionMs,
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

            is NavigationEvent.DiscardSegment -> {
                state.value.mediaController?.let { mediaController ->
                    val currentPosition = mediaController.currentPosition
                    val duration = mediaController.duration
                    val episode = state.value.selectedEpisode
                    val podcast = state.value.selectedPodcast
                    _state.value = state.value.copy(
                        sponsorSectionStart = null,
                        sponsorSectionEnd = null,
                        isPreviewing = PreviewState.NONE,
                        currentPosition = currentPosition,
                        duration = duration,
                    )
                    if (episode != null && podcast != null) {
                        setEpisodeAsMediaItem(
                            episode = episode,
                            podcast = podcast,
                            startPlaying = true,

                            )
                    }
                    mediaController.seekTo(currentPosition)
                    _state.value = state.value.copy(
                        currentPosition = currentPosition,
                        duration = duration,
                    )
                }
            }

            NavigationEvent.SubmitSegment -> {
                viewModelScope.launch {
                    val episodeUrl = state.value.selectedEpisode?.episodeUrl
                    val podcastUrl = state.value.selectedPodcast?.podcast?.url
                    val startPosition = state.value.sponsorSectionStart
                    val endPosition = state.value.sponsorSectionEnd
                    val duration = state.value.mediaController?.duration ?: -1

                    if (
                        episodeUrl != null &&
                        podcastUrl != null &&
                        startPosition != null &&
                        endPosition != null
                    ) {
                        _state.value = state.value.copy(
                            sponsorSectionStart = null,
                            sponsorSectionEnd = null,
                            isPreviewing = PreviewState.NONE,
                        )
                        podcastsUseCases.submitSponsorSectionUseCase(
                            episodeUrl = episodeUrl,
                            podcastUrl = podcastUrl,
                            startPosition = startPosition,
                            endPosition = endPosition,
                            duration = duration,
                        ).firstOrNull()
                        loadSponsorSections(episodeUrl = episodeUrl)
                    }
                }
            }

            is NavigationEvent.RateSponsorSection -> {
                viewModelScope.launch {
                    state.value.mediaController?.let { mediaController ->
                        podcastsUseCases.rateSponsorSectionUseCase(
                            sponsorSectionId = event.sponsorSection.id,
                            isPositive = event.isPositive,
                            duration = mediaController.duration,
                        ).firstOrNull()

                        if (event.isPositive) {
                            mediaController.seekTo(event.sponsorSection.endPosition)
                        } else {

                        }
                        state.value.selectedEpisode?.episodeUrl?.let {
                            loadSponsorSections(episodeUrl = it)
                        }
                    }
                }
            }
        }

    }

    private fun getSettings() {
        viewModelScope.launch {
            settingsUseCases.getSettingsUseCase().firstOrNull()?.let {
                _state.value = state.value.copy(
                    settings = it
                )
            }
        }
    }

    private fun setEpisodeAsMediaItem(
        episode: Episode,
        podcast: PodcastWithRelations,
        startPlaying: Boolean = true,
    ) {
        val mediaItem =
            MediaItem.Builder()
                .setMediaId(episode.episodeUrl)
                .setUri(episode.episodePath)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setArtist(podcast.podcast.title)
                        .setTitle(episode.title)
                        .setArtworkUri(
                            if (state.value.settings.setNotificationImage) {
                                Uri.parse(
                                    episode.imagePath ?: episode.imageUrl
                                )
                            } else {
                                Uri.EMPTY
                            }
                        )
                        .setExtras(Bundle().apply {
                            putString("episodeUrl", episode.episodeUrl)
                            putInt("podcastId", podcast.podcast.id)
                        })
                        .build()
                )
                .build()

        state.value.mediaController?.setMediaItem(mediaItem)
        state.value.mediaController?.prepare()
        state.value.mediaController?.seekTo(episode.lastPosition)
        if (startPlaying) state.value.mediaController?.play()
    }

    private fun loadSponsorSections(episodeUrl: String, schedulePlaybackAction: Boolean = true) {
        viewModelScope.launch {
            episodeUseCases.getSponsorSectionsUseCase(episodeUrl).firstOrNull()
                ?.let { sponsorSections ->
                    setSponsorSections(sponsorSections)
                    if (schedulePlaybackAction) {
                        sponsorSections.forEach { sponsorSection ->
                            if (sponsorSection.rated == 1 || (!sponsorSection.isProvisional && sponsorSection.rated != -1)) {
                                schedulePlaybackAction(
                                    startPositionMs = sponsorSection.startPosition,
                                    endPositionMs = sponsorSection.endPosition,
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun closePodcastEpisode() {
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

    private fun schedulePlaybackAction(startPositionMs: Long, endPositionMs: Long) {
        viewModelScope.launch {
            val command = SessionCommand(Constants.COMMAND_SCHEDULE_EVENT, Bundle.EMPTY)
            val args = Bundle().apply {
                putLong("START_POSITION_MS", startPositionMs)
                putLong("END_POSITION_MS", endPositionMs)
            }
            val result = state.value.mediaController?.sendCustomCommand(command, args)
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

    private fun setSponsorSections(sponsorSections: List<SponsorSection>) {
        _state.value = state.value.copy(
            sponsorSections = sponsorSections,
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

    @OptIn(UnstableApi::class)
    private fun initializeMediaController(context: Context) {
        val sessionToken =
            SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                val mediaController = controllerFuture.get()
                setMediaController(mediaController)

                val mediaItem = mediaController.currentMediaItem
                if (mediaItem != null) {
                    val extras = mediaItem.mediaMetadata.extras
                    viewModelScope.launch {
                        extras?.getString("episodeUrl")?.let {
                            val episode =
                                episodeUseCases.getEpisodeUseCase(episodeUrl = it).firstOrNull()

                            val podcast = podcastsUseCases.getLocalPodcastByIdUseCase(
                                podcastId = extras.getInt(
                                    "podcastId"
                                )
                            ).firstOrNull()

                            if (podcast != null && episode != null) {
                                setCurrentEpisodeAndPodcast(
                                    episode = episode,
                                    podcast = podcast,
                                )
                                updateDuration()
                                startUpdatingPosition()
                                setIsPlaying(mediaController.isPlaying)
                                loadSponsorSections(
                                    episodeUrl = episode.episodeUrl,
                                    schedulePlaybackAction = false,
                                )
                            }
                        }
                    }
                }

                mediaController.addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        setIsPlaying(isPlaying)
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        updateCurrentPosition()
                        when (playbackState) {
                            Player.STATE_IDLE -> setIsPlaying(false)
                            Player.STATE_BUFFERING -> {} // Handle buffering state if needed
                            Player.STATE_READY -> setIsPlaying(mediaController.playWhenReady)
                            Player.STATE_ENDED -> {
                                setIsPlaying(false)
                                closePodcastEpisode()
                            }

                            else -> setIsPlaying(false)

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

                    override fun onPositionDiscontinuity(
                        oldPosition: Player.PositionInfo,
                        newPosition: Player.PositionInfo,
                        reason: Int,
                    ) {
                        updateCurrentPosition()
                    }
                })
            },
            MoreExecutors.directExecutor()
        )
    }
}