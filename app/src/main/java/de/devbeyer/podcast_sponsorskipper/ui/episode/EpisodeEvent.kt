package de.devbeyer.podcast_sponsorskipper.ui.episode

import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode

sealed class EpisodeEvent {
    data class Download(val episode: Episode) : EpisodeEvent()
    data class CancelDownload(val episode: Episode) : EpisodeEvent()
    data class DeleteEpisode(val episode: Episode) : EpisodeEvent()
    data class CompleteEpisode(val episode: Episode) : EpisodeEvent()
}