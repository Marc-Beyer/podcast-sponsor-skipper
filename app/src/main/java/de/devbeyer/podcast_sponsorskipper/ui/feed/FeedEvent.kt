package de.devbeyer.podcast_sponsorskipper.ui.feed

import androidx.compose.ui.unit.DpOffset
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations

sealed class FeedEvent {
    data class OpenMenu(
        val selectedPodcast: PodcastWithRelations,
        val menuOffset: DpOffset,
    ) : FeedEvent()

    object DismissMenu : FeedEvent()
}