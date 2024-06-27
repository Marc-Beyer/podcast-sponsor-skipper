package de.devbeyer.podcast_sponsorskipper.domain.use_cases

import de.devbeyer.podcast_sponsorskipper.domain.LocalDataManager

class SetCompletedGuidedTourUseCase(
    private val localDataManager: LocalDataManager
) {
    suspend operator fun invoke(){
        localDataManager.saveCompletedGuidedTour()
    }
}