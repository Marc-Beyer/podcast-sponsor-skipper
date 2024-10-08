package de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation

import de.devbeyer.podcast_sponsorskipper.domain.SettingKey
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
        val podcastWithRelations: PodcastWithRelations?,
    ) : NavigationEvent()

    data class UpdatePodcast(
        val podcast: Podcast,
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

    data class ChangeCurNavPodcast(
        val podcastWithRelations: PodcastWithRelations?,
    ) : NavigationEvent()


    data class ChangeBooleanSettings(
        val settingKey: SettingKey,
        val value: Boolean,
    ) : NavigationEvent()

    data class ChangeIntSettings(
        val settingKey: SettingKey,
        val value: Int,
    ) : NavigationEvent()

    data class SetNavigateUp(
        val navigateUp: () -> Unit,
    ) : NavigationEvent()

    data class ChangePlaybackSpeed(
        val value: Float,
    ) : NavigationEvent()

    object OpenChangePlaybackSpeed : NavigationEvent()
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
    object UpdateSettings : NavigationEvent()
    object ConfirmUnsubscribe : NavigationEvent()

}