package de.devbeyer.podcast_sponsorskipper.domain

import kotlinx.coroutines.flow.Flow


interface LocalDataManager {
    suspend fun saveCompletedGuidedTour()
    fun readCompletedGuidedTour(): Flow<Boolean>
}