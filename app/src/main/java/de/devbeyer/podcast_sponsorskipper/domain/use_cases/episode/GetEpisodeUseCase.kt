package de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode

import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import kotlinx.coroutines.flow.Flow

class GetEpisodeUseCase(
    private val episodeDao: EpisodeDao,
) {
    operator fun invoke(episodeUrl: String): Flow<Episode?> {
        return episodeDao.getEpisodeByUrl(episodeUrl)
    }
}