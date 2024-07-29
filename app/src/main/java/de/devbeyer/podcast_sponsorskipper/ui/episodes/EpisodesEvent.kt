package de.devbeyer.podcast_sponsorskipper.ui.episodes

import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode

sealed class EpisodesEvent {
    data class Download(val episode: Episode) : EpisodesEvent()
    data class CancelDownload(val episode: Episode, val onCanceled: (Boolean) -> Unit) : EpisodesEvent()
    data class Play(val episode: Episode) : EpisodesEvent()
    object UpdatePodcast : EpisodesEvent()
}