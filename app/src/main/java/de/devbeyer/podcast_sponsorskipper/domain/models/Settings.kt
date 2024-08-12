package de.devbeyer.podcast_sponsorskipper.domain.models

data class Settings(
    val feedGridLayout: Boolean = true,
    val setNotificationImage: Boolean = true,
    val enableMarquee: Boolean = false,
    val enableMarqueeInPlayer: Boolean = true,

    val downloadImages: Boolean = true,
    val coverImageSize: Int = 512,
    val autoDeleteCompletedEpisodes: Boolean = true,

    val forwardTime: Int = 15,
    val rewindTime: Int = 5,
)
