package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import de.devbeyer.podcast_sponsorskipper.data.local.dao.PodcastDao
import de.devbeyer.podcast_sponsorskipper.domain.models.Podcast
import kotlinx.coroutines.flow.Flow

class GetLocalPodcastByUrl(
    private val podcastDao: PodcastDao,
) {
    operator fun invoke(podcastUrl: String): Flow<Podcast?> {
        return podcastDao.getPodcastFromUrl(podcastUrl)
    }
}