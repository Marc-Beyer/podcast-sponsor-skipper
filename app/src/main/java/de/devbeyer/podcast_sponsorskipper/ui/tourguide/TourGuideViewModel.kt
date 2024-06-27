package de.devbeyer.podcast_sponsorskipper.ui.tourguide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.CompletedGuidedTourUseCases
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TourGuideViewModel @Inject constructor(
    private val completedGuidedTourUseCases: CompletedGuidedTourUseCases
): ViewModel() {
    fun onEvent(event: TourGuideEvent){
        when(event){
            is TourGuideEvent.completedTourGuide -> {
                setCompletedTourGuide()
            }
        }
    }

    private fun setCompletedTourGuide() {
        viewModelScope.launch {
            completedGuidedTourUseCases.setCompletedGuidedTourUseCaseval()
        }
    }
}