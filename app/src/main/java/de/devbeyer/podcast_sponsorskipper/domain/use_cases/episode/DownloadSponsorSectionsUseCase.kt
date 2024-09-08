package de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode

import de.devbeyer.podcast_sponsorskipper.data.local.dao.SponsorSectionDao
import de.devbeyer.podcast_sponsorskipper.domain.repositories.BackendRepository
import kotlinx.coroutines.flow.firstOrNull

class DownloadSponsorSectionsUseCase(
    private val sponsorSectionDao: SponsorSectionDao,
    private val backendRepository: BackendRepository
) {
    suspend operator fun invoke(episodeUrl: String, duration: Long) {
        backendRepository.getSponsorSection(episodeUrl = episodeUrl, duration = duration)
            .firstOrNull()?.let { sponsorSections ->
                sponsorSections.forEach { sponsorSection ->
                    sponsorSectionDao.insert(sponsorSection)
                }
            }
    }
}