package de.devbeyer.podcast_sponsorskipper.ui.feed

import androidx.compose.ui.unit.DpOffset
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations

data class FeedState (
    val podcastsWithRelations:List<PodcastWithRelations> = emptyList(),
    val selectedPodcast: PodcastWithRelations? = null,
    val menuOffset: DpOffset = DpOffset.Zero,
    val isMenuExpanded: Boolean = false,
)