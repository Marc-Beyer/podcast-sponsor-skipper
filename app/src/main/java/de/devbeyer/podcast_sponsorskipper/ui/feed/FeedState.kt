package de.devbeyer.podcast_sponsorskipper.ui.feed

import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations

data class FeedState (
    val podcastsWithRelations:List<PodcastWithRelations> = emptyList()
)