package de.devbeyer.podcast_sponsorskipper.domain.models

data class Settings(
    val downloadImages: Boolean = true,
    val feedGridLayout: Boolean = false,
    val setNotificationImage: Boolean = true,
    val autoDeleteCompletedEpisodes: Boolean = true,
    val forwardTime: Int = 15,
    val rewindTime: Int = 5,
    val enableMarquee: Boolean = true,
    val enableMarqueeInPlayer: Boolean = true,
)
