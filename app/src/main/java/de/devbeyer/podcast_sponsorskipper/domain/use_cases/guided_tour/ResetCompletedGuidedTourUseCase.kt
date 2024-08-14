package de.devbeyer.podcast_sponsorskipper.domain.use_cases.guided_tour

import de.devbeyer.podcast_sponsorskipper.domain.LocalDataManager

class ResetCompletedGuidedTourUseCase(
    private val localDataManager: LocalDataManager
) {
    suspend operator fun invoke(){
        localDataManager.saveCompletedGuidedTour(completed = false)
    }
}