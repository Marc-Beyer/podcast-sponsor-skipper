package de.devbeyer.podcast_sponsorskipper.ui.episodes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val podcastsUseCases: PodcastsUseCases
) : ViewModel() {
    private val _state = mutableStateOf(EpisodesState())
    val state: State<EpisodesState> = _state

    fun onEvent(event: EpisodesEvent) {
        when (event) {
            is EpisodesEvent.UpdatePodcast -> {

            }

            is EpisodesEvent.Play -> {

            }
        }
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