package de.devbeyer.podcast_sponsorskipper.domain.use_cases

import de.devbeyer.podcast_sponsorskipper.domain.LocalDataManager
import kotlinx.coroutines.flow.Flow

class GetCompletedGuidedTourUseCase(
    private val localDataManager: LocalDataManager
) {
    operator fun invoke(): Flow<Boolean>{
        return localDataManager.readCompletedGuidedTour()
    }
}