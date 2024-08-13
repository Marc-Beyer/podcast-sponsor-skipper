package de.devbeyer.podcast_sponsorskipper.ui.episode

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
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.EpisodeUseCases
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val episodeUseCases: EpisodeUseCases,
    private val workManager: WorkManager,
) : ViewModel() {
    private val _state = mutableStateOf(EpisodeState())
    val state: State<EpisodeState> = _state

    init {
        updateActiveDownloadUrls()
    }

    fun onEvent(event: EpisodeEvent) {
        when (event) {
            is EpisodeEvent.Download -> {
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
            is EpisodeEvent.CompleteEpisode -> {
                viewModelScope.launch {
                    episodeUseCases.markEpisodeCompleteUseCase(episode = event.episode)
                }
            }
            is EpisodeEvent.DeleteEpisode -> {
                viewModelScope.launch {
                    episodeUseCases.deleteEpisodeUseCase(event.episode)
                }
            }
            is EpisodeEvent.CancelDownload -> {
                val couldCancel = DownloadManager.cancel(event.episode.episodeUrl, event.episode.title)
                updateActiveDownloadUrls()
            }
        }
    }


    private fun updateActiveDownloadUrls(additionalUrl: String? = null) {
        _state.value = state.value.copy(
            activeDownloadUrls = if(additionalUrl != null){
                DownloadManager.getActiveDownloadUrls() + listOf(additionalUrl)
            }else{
                DownloadManager.getActiveDownloadUrls()
            },
        )
    }
    fun setEpisode(episode: Episode, podcastWithRelations: PodcastWithRelations) {
        viewModelScope.launch {
            episodeUseCases.getSponsorSectionsUseCase(episode.episodeUrl).firstOrNull()?.let { sponsorSections ->
                _state.value = state.value.copy(
                    episode = episode,
                    podcastWithRelations = podcastWithRelations,
                    sponsorSections = sponsorSections
                )
            }
        }
        observeEpisodeChanges(episode.episodeUrl)
    }

    private fun observeEpisodeChanges(episodeUrl: String) {
        viewModelScope.launch {
            episodeUseCases.getEpisodeUseCase(episodeUrl).collect { updatedEpisode ->
                if(updatedEpisode?.episodePath != state.value.episode?.episodePath){
                    _state.value = state.value.copy(episode = updatedEpisode)
                }
            }
        }
    }
}