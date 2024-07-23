package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import androidx.paging.PagingData
import de.devbeyer.podcast_sponsorskipper.domain.models.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.repositories.PodcastRepository
import kotlinx.coroutines.flow.Flow

class GetRemotePodcastsUseCase(
    private val podcastRepository: PodcastRepository
) {
    operator fun invoke(search: String): Flow<PagingData<PodcastWithRelations>> {
        return podcastRepository.getPodcasts(search)
    }
}