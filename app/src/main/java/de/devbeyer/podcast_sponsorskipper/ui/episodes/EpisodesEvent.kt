package de.devbeyer.podcast_sponsorskipper.ui.episodes

import androidx.compose.ui.unit.DpOffset
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode

sealed class EpisodesEvent {
    data class Download(val episode: Episode) : EpisodesEvent()
    data class CancelDownload(val episode: Episode) : EpisodesEvent()
    data class DeleteEpisode(val episode: Episode) : EpisodesEvent()
    data class CompleteEpisode(val episode: Episode) : EpisodesEvent()
    data class OpenMenu(
        val selectedEpisode: Episode,
        val menuOffset: DpOffset,
    ) : EpisodesEvent()

    object UpdatePodcast : EpisodesEvent()
    object DismissMenu : EpisodesEvent()
}