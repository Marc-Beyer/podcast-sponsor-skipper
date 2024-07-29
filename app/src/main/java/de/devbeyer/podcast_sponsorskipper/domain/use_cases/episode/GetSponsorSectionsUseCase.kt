package de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode

import de.devbeyer.podcast_sponsorskipper.data.local.dao.SponsorSectionDao
import de.devbeyer.podcast_sponsorskipper.domain.models.db.SponsorSection
import kotlinx.coroutines.flow.Flow

class GetSponsorSectionsUseCase(
    private val sponsorSectionDao: SponsorSectionDao,
) {
    operator fun invoke(episodeUrl: String): Flow<List<SponsorSection>> {
        return sponsorSectionDao.getSponsorSectionsOfEpisode(episodeUrl)
    }
}