package de.devbeyer.podcast_sponsorskipper.domain.use_cases.user

import de.devbeyer.podcast_sponsorskipper.domain.LocalDataManager
import de.devbeyer.podcast_sponsorskipper.domain.models.UserData
import de.devbeyer.podcast_sponsorskipper.domain.repositories.BackendRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class GetUserUseCase(
    private val localDataManager: LocalDataManager,
    private val backendRepository: BackendRepository,
) {
    operator fun invoke(): Flow<UserData?> = flow {
        val username = localDataManager.readUsername().firstOrNull()
        val token = localDataManager.readToken().firstOrNull()
        if(username == null || token == null){
            backendRepository.register().firstOrNull()?.let {userData ->
                localDataManager.saveUsername(userData.username)
                localDataManager.saveToken(userData.token)
                emit(userData)
            }
        }else{
            emit(UserData(username, token))
        }
    }
}