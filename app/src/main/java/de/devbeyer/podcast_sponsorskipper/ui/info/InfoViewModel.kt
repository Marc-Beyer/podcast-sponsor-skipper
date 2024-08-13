package de.devbeyer.podcast_sponsorskipper.ui.info

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.data.worker.UpdateWorker
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val podcastsUseCases: PodcastsUseCases,
    private val workManager: WorkManager,
) : ViewModel() {
    private val _state = mutableStateOf(InfoState())
    val state: State<InfoState> = _state


    fun onEvent(event: InfoEvent) {
        when (event) {
            is InfoEvent.SubscribeToPodcast -> {
                setLoading(true)

                val workData = workDataOf(
                    "url" to event.podcastWithRelations.podcast.url,
                    "title" to event.podcastWithRelations.podcast.title,
                )
                val updateWorkRequest = OneTimeWorkRequestBuilder<UpdateWorker>()
                    .setInputData(workData)
                    .build()
                workManager.enqueue(updateWorkRequest)
            }

            is InfoEvent.UnsubscribeFromPodcast -> {
                viewModelScope.launch {
                    podcastsUseCases.deleteLocalPodcastUseCase(event.podcastWithRelations.podcast)
                }
            }
        }

    }

    fun setPodcast(podcastWithRelations: PodcastWithRelations) {
        viewModelScope.launch {
            podcastsUseCases.getLocalPodcastByUrlUseCase(podcastWithRelations.podcast.url)
                .collect { localePodcastWithRelations ->
                    val subscribedToPodcast = localePodcastWithRelations != null
                    _state.value = state.value.copy(
                        podcastWithRelations = localePodcastWithRelations ?: podcastWithRelations,
                        subscribedToPodcast = subscribedToPodcast,
                    )
                    if (subscribedToPodcast) setLoading(false)
                }
        }
    }

    private fun setLoading(value: Boolean) {
        _state.value = state.value.copy(
            isLoading = value
        )
    }
}