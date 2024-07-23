package de.devbeyer.podcast_sponsorskipper.ui.search

import androidx.paging.PagingData
import de.devbeyer.podcast_sponsorskipper.domain.models.PodcastWithRelations
import kotlinx.coroutines.flow.Flow


data class SearchState(
    val search: String = "",
    val podcastsWithRelations: Flow<PagingData<PodcastWithRelations>>? = null
)
