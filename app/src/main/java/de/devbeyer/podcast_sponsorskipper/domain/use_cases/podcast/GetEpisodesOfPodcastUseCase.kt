package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import kotlinx.coroutines.flow.Flow

class GetEpisodesOfPodcastUseCase(
    private val episodeDao: EpisodeDao,
) {
    operator fun invoke(podcastId: Int): Flow<List<Episode>> {
        return episodeDao.getEpisodesByPodcastId(podcastId)
    }
}