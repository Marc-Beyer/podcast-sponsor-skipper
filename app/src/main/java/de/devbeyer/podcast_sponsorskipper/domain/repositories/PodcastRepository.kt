package de.devbeyer.podcast_sponsorskipper.domain.repositories

import androidx.paging.PagingData
import de.devbeyer.podcast_sponsorskipper.domain.models.Podcast
import kotlinx.coroutines.flow.Flow

interface PodcastRepository {
    fun getPodcasts(search: String):Flow<PagingData<Podcast>>
    fun searchPodcasts(search: String):Flow<PagingData<Podcast>>
}