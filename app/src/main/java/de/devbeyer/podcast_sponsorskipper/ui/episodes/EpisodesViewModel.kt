package de.devbeyer.podcast_sponsorskipper.ui.episodes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.data.worker.DownloadEpisodeWorker
import de.devbeyer.podcast_sponsorskipper.data.worker.DownloadManager
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.EpisodeUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.FileUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val podcastsUseCases: PodcastsUseCases,
    private val episodeUseCases: EpisodeUseCases,
    private val fileUseCases: FileUseCases,
    private val workManager: WorkManager,
) : ViewModel() {
    private val _state = mutableStateOf(EpisodesState())
    val state: State<EpisodesState> = _state

    init {
        updateActiveDownloadUrls()
    }

    fun onEvent(event: EpisodesEvent) {
        when (event) {
            is EpisodesEvent.UpdatePodcast -> {

            }

            is EpisodesEvent.Download -> {
                val workData = workDataOf(
                    "url" to event.episode.episodeUrl,
                    "title" to event.episode.title
                )
                val downloadWorkRequest = OneTimeWorkRequestBuilder<DownloadEpisodeWorker>()
                    .setInputData(workData)
                    .build()
                workManager.enqueue(
                    downloadWorkRequest
                )
                updateActiveDownloadUrls(event.episode.episodeUrl)
            }

            is EpisodesEvent.OpenMenu -> {
                _state.value = state.value.copy(
                    selectedEpisode = event.selectedEpisode,
                    menuOffset = event.menuOffset,
                    isMenuExpanded = true,
                )
            }

            is EpisodesEvent.DismissMenu -> {
                dismissMenu()
            }

            is EpisodesEvent.SetFilterMenuExpanded -> {
                _state.value = state.value.copy(
                    isFilterMenuExpanded = event.expanded,
                )
            }

            is EpisodesEvent.SetFilter -> {
                _state.value = state.value.copy(
                    activeFilter = event.episodesFilter,
                    isFilterMenuExpanded = false,
                )
            }

            is EpisodesEvent.CompleteEpisode -> {
                viewModelScope.launch {
                    episodeUseCases.markEpisodeCompleteUseCase(episode = event.episode)
                }
                dismissMenu()
            }

            is EpisodesEvent.Favorite -> {
                viewModelScope.launch {
                    episodeUseCases.favoriteEpisodeUseCase(
                        episode = event.episode,
                        favorite = event.favorite
                    )
                }
                dismissMenu()
            }

            is EpisodesEvent.CompleteEpisodesFromHere -> {
                viewModelScope.launch {
                    var markAsComplete = false
                    for (episode in state.value.episodes) {
                        if (episode.episodeUrl == event.episode.episodeUrl) markAsComplete = true
                        if (markAsComplete) {
                            episodeUseCases.markEpisodeCompleteUseCase(
                                episode = episode,
                                toggleStatus = false,
                            )
                        }
                    }
                }
                dismissMenu()
            }

            is EpisodesEvent.DeleteEpisode -> {
                viewModelScope.launch {
                    episodeUseCases.deleteEpisodeUseCase(event.episode)
                }
                dismissMenu()
            }

            is EpisodesEvent.CancelDownload -> {
                val couldCancel =
                    DownloadManager.cancel(event.episode.episodeUrl, event.episode.title)
                updateActiveDownloadUrls()
            }
        }
    }

    private fun dismissMenu() {
        _state.value = state.value.copy(
            selectedEpisode = null,
            isMenuExpanded = false,
        )
        updateActiveDownloadUrls()
    }


    private fun updateActiveDownloadUrls(additionalUrl: String? = null) {
        _state.value = state.value.copy(
            activeDownloadUrls = if (additionalUrl != null) {
                DownloadManager.getActiveDownloadUrls() + listOf(additionalUrl)
            } else {
                DownloadManager.getActiveDownloadUrls()
            },
        )
    }

    fun setPodcast(podcastWithRelations: PodcastWithRelations) {
        viewModelScope.launch {
            podcastsUseCases.getEpisodesOfPodcastUseCase(podcastWithRelations.podcast.id)
                .collect { episodes ->
                    _state.value = state.value.copy(
                        podcastWithRelations = podcastWithRelations,
                        episodes = episodes,
                    )
                }
        }
    }
}