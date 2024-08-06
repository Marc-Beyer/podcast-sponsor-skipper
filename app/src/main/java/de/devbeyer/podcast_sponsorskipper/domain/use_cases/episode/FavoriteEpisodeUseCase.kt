package de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode

import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode

class FavoriteEpisodeUseCase(
    private val episodeDao: EpisodeDao,
) {
    suspend operator fun invoke(episode: Episode, favorite: Boolean = true) {
        episodeDao.update(
            episode.copy(
                favorite = favorite,
            )
        )
    }
}