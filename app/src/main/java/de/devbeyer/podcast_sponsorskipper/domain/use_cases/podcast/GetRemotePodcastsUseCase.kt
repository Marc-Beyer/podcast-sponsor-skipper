package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import androidx.paging.PagingData
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.repositories.BackendRepository
import kotlinx.coroutines.flow.Flow

class GetRemotePodcastsUseCase(
    private val backendRepository: BackendRepository
) {
    operator fun invoke(search: String): Flow<PagingData<PodcastWithRelations>> {
        return backendRepository.getPodcasts(search)
    }
}