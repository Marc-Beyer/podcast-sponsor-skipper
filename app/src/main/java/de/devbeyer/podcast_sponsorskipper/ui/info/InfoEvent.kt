package de.devbeyer.podcast_sponsorskipper.ui.info

sealed class InfoEvent {
    object AddPodcastToFeed: InfoEvent()
}