package de.devbeyer.podcast_sponsorskipper.ui.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val podcastsUseCases: PodcastsUseCases
) : ViewModel() {
    private val _state = mutableStateOf(SearchState())
    val state: State<SearchState> = _state

    init {
        searchPodcast()
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.changeSearch -> {
                _state.value = state.value.copy(search = event.search)
            }

            is SearchEvent.SearchPodcast -> {
                searchPodcast()
            }
        }

    }

    private fun searchPodcast() {
        val podcasts =
            podcastsUseCases.getRemotePodcastsUseCase(state.value.search).cachedIn(viewModelScope)
        _state.value = state.value.copy(podcastsWithRelations = podcasts)
    }
}