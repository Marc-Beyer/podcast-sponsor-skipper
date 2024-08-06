package de.devbeyer.podcast_sponsorskipper.ui.navigation

import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.models.db.SponsorSection

sealed class NavigationEvent {
    data class PlayEpisode(
        val episode: Episode,
        val podcast: PodcastWithRelations,
    ) : NavigationEvent()

    data class SeekTo(
        val position: Long,
    ) : NavigationEvent()

    data class Unsubscribe(
        val podcastWithRelations: PodcastWithRelations?
    ) : NavigationEvent()

    data class UpdatePodcast(
        val podcast: Podcast
    ) : NavigationEvent()

    data class RateSponsorSection(
        val sponsorSection: SponsorSection,
        val isPositive: Boolean,
    ) : NavigationEvent()

    data class Favorite(
        val episode: Episode,
        val favorite: Boolean,
    ) : NavigationEvent()

    data class ChangeCurNavEpisode(
        val episode: Episode,
    ) : NavigationEvent()

    object Play : NavigationEvent()
    object Stop : NavigationEvent()
    object SkipForward : NavigationEvent()
    object SkipBack : NavigationEvent()
    object Close : NavigationEvent()
    object StartSponsorSection : NavigationEvent()
    object EndSponsorSection : NavigationEvent()
    object Preview : NavigationEvent()
    object SubmitSegment : NavigationEvent()
    object DiscardSegment : NavigationEvent()
    object UpdatePodcasts : NavigationEvent()

}