package de.devbeyer.podcast_sponsorskipper.domain.use_cases.backend

import androidx.paging.PagingData
import de.devbeyer.podcast_sponsorskipper.domain.models.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.repositories.PodcastRepository
import kotlinx.coroutines.flow.Flow

class GetPodcastsUseCase(
    private val podcastRepository: PodcastRepository
) {
    operator fun invoke(sources: List<String>): Flow<PagingData<Podcast>> {
        return podcastRepository.getPodcasts(sources)
    }
}