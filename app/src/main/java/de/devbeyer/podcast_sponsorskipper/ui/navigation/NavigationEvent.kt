package de.devbeyer.podcast_sponsorskipper.ui.navigation

import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode

sealed class NavigationEvent {
        data class PlayEpisode(val episode: Episode) : NavigationEvent()
    }