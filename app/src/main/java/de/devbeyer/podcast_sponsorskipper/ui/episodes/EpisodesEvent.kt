package de.devbeyer.podcast_sponsorskipper.ui.episodes

import androidx.compose.ui.unit.DpOffset
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode

sealed class EpisodesEvent {
    data class Download(val episode: Episode) : EpisodesEvent()
    data class CancelDownload(val episode: Episode) : EpisodesEvent()
    data class DeleteEpisode(val episode: Episode) : EpisodesEvent()
    data class CompleteEpisode(val episode: Episode) : EpisodesEvent()
    data class CompleteEpisodesFromHere(val episode: Episode) : EpisodesEvent()
    data class SetFilter(val episodesFilter: EpisodeFilter) : EpisodesEvent()
    data class SetFilterMenuExpanded(val expanded: Boolean) : EpisodesEvent()
    data class OpenMenu(
        val selectedEpisode: Episode,
        val menuOffset: DpOffset,
    ) : EpisodesEvent()
    data class Favorite(
        val episode: Episode,
        val favorite: Boolean,
    ) : EpisodesEvent()


    object UpdatePodcast : EpisodesEvent()
    object DismissMenu : EpisodesEvent()

}

enum class EpisodeFilter {
    ALL,
    DOWNLOADED,
    INCOMPLETE,
    RECENT,
    FAVORITE,
}