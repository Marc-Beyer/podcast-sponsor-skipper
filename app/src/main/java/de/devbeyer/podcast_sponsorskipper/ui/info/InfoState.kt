package de.devbeyer.podcast_sponsorskipper.ui.info

import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations

data class InfoState (
    val podcastWithRelations: PodcastWithRelations? = null,
    val subscribedToPodcast:Boolean = false
)
