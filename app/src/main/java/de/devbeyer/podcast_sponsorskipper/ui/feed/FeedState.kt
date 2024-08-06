package de.devbeyer.podcast_sponsorskipper.ui.feed

import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import kotlinx.coroutines.flow.StateFlow

data class FeedState (
    val podcastsWithRelations:List<PodcastWithRelations> = emptyList()
)