package de.devbeyer.podcast_sponsorskipper.domain

import kotlinx.coroutines.flow.Flow


interface LocalDataManager {
    suspend fun saveCompletedGuidedTour()
    fun readCompletedGuidedTour(): Flow<Boolean>
    suspend fun saveUsername(username: String)
    fun readUsername(): Flow<String?>
    suspend fun saveToken(token: String)
    fun readToken(): Flow<String?>
}