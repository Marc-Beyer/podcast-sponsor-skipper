package de.devbeyer.podcast_sponsorskipper.ui.info

import de.devbeyer.podcast_sponsorskipper.domain.models.PodcastWithRelations

sealed class InfoEvent {
    data class SubscribeToPodcast(val podcastWithRelations: PodcastWithRelations) : InfoEvent()
    data class UnsubscribeFromPodcast(val podcastWithRelations: PodcastWithRelations) : InfoEvent()
}