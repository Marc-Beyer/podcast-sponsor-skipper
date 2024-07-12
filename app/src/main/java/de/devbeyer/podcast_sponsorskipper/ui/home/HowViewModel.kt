package de.devbeyer.podcast_sponsorskipper.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.backend.PodcastsUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.guided_tour.CompletedGuidedTourUseCases
import javax.inject.Inject

@HiltViewModel
class HowViewModel @Inject constructor(
    private val podcastsUseCases: PodcastsUseCases
): ViewModel() {
val podcasts = podcastsUseCases.getPodcastsUseCase().cachedIn(viewModelScope)
}