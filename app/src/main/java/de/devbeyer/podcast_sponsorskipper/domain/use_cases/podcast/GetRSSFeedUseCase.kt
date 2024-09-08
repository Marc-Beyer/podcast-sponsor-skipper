package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastAndEpisodes
import de.devbeyer.podcast_sponsorskipper.domain.repositories.BackendRepository
import kotlinx.coroutines.flow.Flow

class GetRSSFeedUseCase(
    private val backendRepository: BackendRepository
) {
    operator fun invoke(rssFeedUrl: String): Flow<PodcastAndEpisodes?> {
        return backendRepository.getRSSFeed(rssFeedUrl)
    }
}