package de.devbeyer.podcast_sponsorskipper.ui.feed

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val podcastsUseCases: PodcastsUseCases
) : ViewModel() {
    private val _state = mutableStateOf(FeedState())
    val state: State<FeedState> = _state

    init{
        getPodcasts()
    }

    fun onEvent(event: FeedEvent) {
        when (event) {
            is FeedEvent.DismissMenu -> {
                _state.value = state.value.copy(
                    isMenuExpanded = false,
                )
            }

            is FeedEvent.OpenMenu -> {
                _state.value = state.value.copy(
                    isMenuExpanded = true,
                    menuOffset = event.menuOffset,
                    selectedPodcast = event.selectedPodcast,
                )
            }
        }

    }

    private fun getPodcasts() {
        podcastsUseCases.getLocalPodcastsUseCase().onEach {
            _state.value = state.value.copy(podcastsWithRelations = it)
        }.launchIn(viewModelScope)
    }

}