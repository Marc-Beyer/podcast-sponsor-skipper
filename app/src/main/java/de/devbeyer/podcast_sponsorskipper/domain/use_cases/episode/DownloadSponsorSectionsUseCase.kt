package de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode

import android.util.Log
import de.devbeyer.podcast_sponsorskipper.data.local.dao.SponsorSectionDao
import de.devbeyer.podcast_sponsorskipper.domain.repositories.BackendRepository
import kotlinx.coroutines.flow.firstOrNull

class DownloadSponsorSectionsUseCase(
    private val sponsorSectionDao: SponsorSectionDao,
    private val backendRepository: BackendRepository
) {
    suspend operator fun invoke(episodeUrl: String, duration: Long) {
        Log.i("AAA", "Download sponsorSections for $episodeUrl")
        backendRepository.getSponsorSection(episodeUrl = episodeUrl, duration = duration)
            .firstOrNull()?.let { sponsorSections ->
                Log.i("AAA", sponsorSections.toString())
                sponsorSections.forEach { sponsorSection ->
                    Log.i("AAA", sponsorSection.toString())
                    sponsorSectionDao.insert(sponsorSection)
                    Log.i("AAA", "Downloaded sponsorSection $sponsorSection")
                }
            }
    }
}