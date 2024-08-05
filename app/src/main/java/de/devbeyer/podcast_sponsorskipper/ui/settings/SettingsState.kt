package de.devbeyer.podcast_sponsorskipper.ui.settings

import androidx.paging.PagingData
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import kotlinx.coroutines.flow.Flow

data class SettingsState(
    val search: String = "",
    val rssFeedUrl: String = "",
    val podcastsWithRelations: Flow<PagingData<PodcastWithRelations>>? = null
)
