package de.devbeyer.podcast_sponsorskipper.ui.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
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
            is SearchEvent.ChangeSearch -> {
                _state.value = state.value.copy(search = event.search)
            }

            is SearchEvent.SearchPodcast -> {
                searchPodcast()
            }

            is SearchEvent.ChangeRSSFeedUrl -> {
                _state.value = state.value.copy(rssFeedUrl = event.rssFeedUrl)
            }

            is SearchEvent.AddRSSFeed -> {
                addRSSFeedPodcast()
            }
        }

    }

    private fun searchPodcast() {
        val podcasts =
            podcastsUseCases.getRemotePodcastsUseCase(state.value.search).cachedIn(viewModelScope)
        _state.value = state.value.copy(podcastsWithRelations = podcasts)
    }

    private fun addRSSFeedPodcast() {
        viewModelScope.launch {
            podcastsUseCases.getRSSFeed(state.value.rssFeedUrl).firstOrNull()?.let {
                podcastsUseCases.insertPodcastUseCase(it)
            }
        }
    }
}