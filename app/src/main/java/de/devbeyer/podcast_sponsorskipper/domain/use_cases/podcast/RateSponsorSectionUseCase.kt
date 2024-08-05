package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import android.util.Log
import de.devbeyer.podcast_sponsorskipper.data.local.dao.SponsorSectionDao
import de.devbeyer.podcast_sponsorskipper.domain.repositories.BackendRepository
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.user.UserUseCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class RateSponsorSectionUseCase(
    private val backendRepository: BackendRepository,
    private val userUseCases: UserUseCases,
    private val sponsorSectionDao: SponsorSectionDao,
) {
    operator fun invoke(sponsorSectionId: Long, duration: Long, isPositive: Boolean): Flow<Long?> =
        flow {
            Log.i("AAA", "RateSponsorSectionUseCase")
            userUseCases.getUserUseCase().firstOrNull()?.let { userData ->
                backendRepository.rateSponsorSection(
                    sponsorSectionId = sponsorSectionId,
                    isPositive = isPositive,
                    duration = duration,
                    username = userData.username,
                    token = userData.token,
                ).firstOrNull()

                Log.i("AAA", "RATE $sponsorSectionId $isPositive")
                sponsorSectionDao.markAsRated(
                    sponsorSectionId = sponsorSectionId,
                    rating = if (isPositive) 1 else -1
                )
                emit(sponsorSectionId)

            }
            emit(null)
        }
}