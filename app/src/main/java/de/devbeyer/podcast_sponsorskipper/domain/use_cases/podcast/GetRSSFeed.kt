package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import android.util.Log
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastAndEpisodes
import de.devbeyer.podcast_sponsorskipper.domain.repositories.BackendRepository
import kotlinx.coroutines.flow.Flow

class GetRSSFeed(
    private val backendRepository: BackendRepository
) {
    operator fun invoke(rssFeedUrl: String): Flow<PodcastAndEpisodes?> {
        Log.i("Update", "rssFeedUrl $rssFeedUrl")
        return backendRepository.getRSSFeed(rssFeedUrl)
    }
}