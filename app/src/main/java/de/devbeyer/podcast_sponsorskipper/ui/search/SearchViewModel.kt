package de.devbeyer.podcast_sponsorskipper.ui.search

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.data.worker.UpdateWorker
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import de.devbeyer.podcast_sponsorskipper.util.openLink
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val podcastsUseCases: PodcastsUseCases,
    private val workManager: WorkManager,
    private val application: Application,
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

            is SearchEvent.SearchOnline -> {
                application.openLink(
                    url = "https://www.google.com/search?q=${Uri.encode(state.value.search + " podcast rss")}",
                    addFlags = true,
                )
            }
        }

    }

    private fun searchPodcast() {
        val podcasts =
            podcastsUseCases.getRemotePodcastsUseCase(state.value.search).cachedIn(viewModelScope)
        _state.value = state.value.copy(podcastsWithRelations = podcasts)
    }

    private fun addRSSFeedPodcast() {
        val workData = workDataOf(
            "url" to state.value.rssFeedUrl,
            "title" to state.value.rssFeedUrl,
        )
        val updateWorkRequest = OneTimeWorkRequestBuilder<UpdateWorker>()
            .setInputData(workData)
            .build()
        workManager.enqueue(updateWorkRequest)
    }
}