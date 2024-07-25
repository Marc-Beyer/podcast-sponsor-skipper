package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.repositories.PodcastRepository
import kotlinx.coroutines.flow.Flow

class GetRSSFeed(
    private val podcastRepository: PodcastRepository
) {
    operator fun invoke(rssFeedUrl: String): Flow<PodcastWithRelations> {
        return podcastRepository.getRSSFeed(rssFeedUrl)
    }
}