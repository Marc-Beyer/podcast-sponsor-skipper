package de.devbeyer.podcast_sponsorskipper.domain.use_cases.user

import de.devbeyer.podcast_sponsorskipper.domain.LocalDataManager
import de.devbeyer.podcast_sponsorskipper.domain.models.UserData
import de.devbeyer.podcast_sponsorskipper.domain.repositories.BackendRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class RegisterUseCase(
    private val localDataManager: LocalDataManager,
    private val backendRepository: BackendRepository,
) {
    suspend operator fun invoke(): Flow<UserData> = flow {
        backendRepository.register().firstOrNull()?.let {userData ->
            localDataManager.saveUsername(userData.username)
            localDataManager.saveToken(userData.token)
            emit(userData)
        }
    }
}