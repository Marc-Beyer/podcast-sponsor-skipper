package de.devbeyer.podcast_sponsorskipper.ui.episode

import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode

sealed class EpisodeEvent {
    data class Play(val episode: Episode): EpisodeEvent()
    object UpdatePodcast: EpisodeEvent()
}