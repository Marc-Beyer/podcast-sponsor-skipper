package de.devbeyer.podcast_sponsorskipper.ui.info

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.models.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import javax.inject.Inject

    @HiltViewModel
    class InfoViewModel @Inject constructor(
        private val podcastsUseCases: PodcastsUseCases
    ) : ViewModel() {
        private val _state = mutableStateOf(InfoState())
        val state: State<InfoState> = _state


        fun onEvent(event: InfoEvent) {
            when (event) {
                is InfoEvent.AddPodcastToFeed -> {

                }
            }

        }

        fun setPodcast(podcastWithRelations: PodcastWithRelations) {
            _state.value = state.value.copy(podcastWithRelations = podcastWithRelations)
        }
    }