package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import de.devbeyer.podcast_sponsorskipper.data.local.dao.PodcastDao
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import kotlinx.coroutines.flow.Flow

class GetLocalPodcastsUseCase  (
    private val podcastDao: PodcastDao
) {
    operator fun invoke(): Flow<List<PodcastWithRelations>> {
        return podcastDao.getPodcasts()
    }
}