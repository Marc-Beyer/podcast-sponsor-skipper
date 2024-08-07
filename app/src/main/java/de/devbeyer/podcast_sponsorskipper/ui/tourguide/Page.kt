package de.devbeyer.podcast_sponsorskipper.ui.tourguide

import androidx.annotation.DrawableRes
import de.devbeyer.podcast_sponsorskipper.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val imageDark: Int,
    @DrawableRes val imageLight: Int,
)

val pages = listOf(
    Page(
        title = "Welcome",
        description = "Easily skip over sponsor sections and enjoy uninterrupted listening.",
        imageDark = R.drawable.podcasts_white,
        imageLight = R.drawable.podcasts_black,
    ),
    Page(
        title = "Sponsor Skipping",
        description = "The app allows you to mark sponsor segments and automatically skip them during playback, providing a seamless podcast experience.",
        imageDark = R.drawable.podcasts_white,
        imageLight = R.drawable.podcasts_black,
    ),
    Page(
        title = "Notifications",
        description = "Enable notifications to maintain uninterrupted playback and to control your podcasts even when your phone is locked.",
        imageDark = R.drawable.notifications_white,
        imageLight = R.drawable.notifications_black,
    ),
)