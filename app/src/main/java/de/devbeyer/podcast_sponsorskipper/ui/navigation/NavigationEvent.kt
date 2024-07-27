package de.devbeyer.podcast_sponsorskipper.ui.navigation

import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations

sealed class NavigationEvent {
    data class PlayEpisode(
        val episode: Episode,
        val podcast: PodcastWithRelations
    ) : NavigationEvent()
    object Play: NavigationEvent()
    object Stop: NavigationEvent()
    object SkipForward: NavigationEvent()
    object SkipBack: NavigationEvent()
}