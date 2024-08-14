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
        title = "Approved Sponsor Segments",
        description = "The red section highlights sponsor segments that have been approved by enough users. These segments will be automatically skipped during playback.",
        imageDark = R.drawable.player_explain_red_dark,
        imageLight = R.drawable.player_explain_red,
    ),
    Page(
        title = "Unapproved Sponsor Segments",
        description = "The gray section marks sponsor segments that are not yet approved. These segments will not be skipped automatically. You can give them a thumbs up to help them get approved and skipped in the future.",
        imageDark = R.drawable.player_explain_gray_dark,
        imageLight = R.drawable.player_explain_gray,
    ),
    Page(
        title = "Mark Sponsor Segments",
        description = "Press the \"AD\" button during playback to mark sponsor sections. Your input helps improve automatic skipping for everyone.",
        imageDark = R.drawable.player_explain_ad_dark,
        imageLight = R.drawable.player_explain_ad,
    ),
    Page(
        title = "Notifications",
        description = "Enable notifications to maintain uninterrupted playback and to control your podcasts even when your phone is locked.",
        imageDark = R.drawable.notifications_white,
        imageLight = R.drawable.notifications_black,
    ),
)