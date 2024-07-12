package de.devbeyer.podcast_sponsorskipper.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import de.devbeyer.podcast_sponsorskipper.data.remote.BackendAPI
import de.devbeyer.podcast_sponsorskipper.data.remote.PodcastPagingSource
import de.devbeyer.podcast_sponsorskipper.domain.models.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.repositories.PodcastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class PodcastRepositoryImpl(
    private val backendAPI: BackendAPI
): PodcastRepository {
    override fun getPodcasts(): Flow<PagingData<Podcast>> {
        return Pager(
            config = PagingConfig(pageSize = 12),
            pagingSourceFactory = {
                PodcastPagingSource(backendAPI)
            }
        ).flow
    }
}