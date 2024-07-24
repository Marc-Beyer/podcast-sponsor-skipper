package de.devbeyer.podcast_sponsorskipper.ui.info

import de.devbeyer.podcast_sponsorskipper.domain.models.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.search.SearchEvent

sealed class InfoEvent {
    data class subscribeToPodcast(val podcastWithRelations: PodcastWithRelations): InfoEvent()
}