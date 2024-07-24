package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import de.devbeyer.podcast_sponsorskipper.data.local.dao.PodcastDao
import de.devbeyer.podcast_sponsorskipper.domain.models.Podcast

class DeleteLocalPodcast(
    private val podcastDao: PodcastDao
) {
    suspend operator fun invoke(podcast: Podcast) {
        podcastDao.deletePodcast(podcast)
    }
}