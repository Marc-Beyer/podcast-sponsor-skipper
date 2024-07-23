package de.devbeyer.podcast_sponsorskipper.ui.feed

import de.devbeyer.podcast_sponsorskipper.domain.models.PodcastWithRelations

data class FeedState (
    val podcasts:List<PodcastWithRelations> = emptyList()
)