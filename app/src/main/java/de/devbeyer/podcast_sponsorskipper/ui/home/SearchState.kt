package de.devbeyer.podcast_sponsorskipper.ui.home

import androidx.paging.PagingData
import de.devbeyer.podcast_sponsorskipper.domain.models.Podcast
import kotlinx.coroutines.flow.Flow


data class SearchState(
    val search: String = "",
    val podcasts: Flow<PagingData<Podcast>>? = null
)
