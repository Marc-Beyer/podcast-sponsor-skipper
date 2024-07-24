package de.devbeyer.podcast_sponsorskipper.ui.info

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.models.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val podcastsUseCases: PodcastsUseCases
) : ViewModel() {
    private val _state = mutableStateOf(InfoState())
    val state: State<InfoState> = _state


    fun onEvent(event: InfoEvent) {
        when (event) {
            is InfoEvent.subscribeToPodcast -> {
                viewModelScope.launch {
                    podcastsUseCases.insertPodcastUseCase(event.podcastWithRelations)
                }
            }
        }

    }

    fun setPodcast(podcastWithRelations: PodcastWithRelations) {
        viewModelScope.launch {
            podcastsUseCases.getLocalPodcastByUrl(podcastWithRelations.podcast.url)
                .collect { podcast ->
                    _state.value = state.value.copy(
                        podcastWithRelations = podcastWithRelations,
                        subscribedToPodcast = podcast != null
                    )
                }
        }
    }
}