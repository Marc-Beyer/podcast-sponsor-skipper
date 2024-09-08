package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import de.devbeyer.podcast_sponsorskipper.data.local.dao.SponsorSectionDao
import de.devbeyer.podcast_sponsorskipper.domain.models.db.SponsorSection
import de.devbeyer.podcast_sponsorskipper.domain.repositories.BackendRepository
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.user.UserUseCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class SubmitSponsorSectionUseCase(
    private val backendRepository: BackendRepository,
    private val userUseCases: UserUseCases,
    private val sponsorSectionDao: SponsorSectionDao,
) {
    suspend operator fun invoke(
        episodeUrl: String,
        podcastUrl: String,
        startPosition: Long,
        endPosition: Long,
        duration: Long,
    ): Flow<SponsorSection?> = flow {
        userUseCases.getUserUseCase().firstOrNull()?.let { userData ->
            backendRepository.submitSponsorSection(
                episodeUrl = episodeUrl,
                podcastUrl = podcastUrl,
                startPosition = startPosition,
                endPosition = endPosition,
                duration = duration,
                username = userData.username,
                token = userData.token
            ).firstOrNull()?.let { sponsorSectionId ->
                val sponsorSection = SponsorSection(
                    id = sponsorSectionId,
                    episodeUrl = episodeUrl,
                    startPosition = startPosition,
                    endPosition = endPosition,
                    rated = 1,
                    isProvisional = false,
                )
                sponsorSectionDao.insert(sponsorSection)
                emit(sponsorSection)
            }
        }
        emit(null)
    }
}