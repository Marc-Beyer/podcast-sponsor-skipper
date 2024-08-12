package de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode

import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode

class UpdateEpisodeUseCase(
    private val episodeDao: EpisodeDao,
) {
    suspend operator fun invoke(episode: Episode) {
        episodeDao.update( episode )
    }
}