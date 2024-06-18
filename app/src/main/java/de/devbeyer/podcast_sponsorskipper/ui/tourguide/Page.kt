package de.devbeyer.podcast_sponsorskipper.ui.tourguide

import androidx.annotation.DrawableRes
import de.devbeyer.podcast_sponsorskipper.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int,
)

val pages = listOf(
    Page(
        title = "Test 1",
        description = "This is a test!",
        R.drawable.podcasts_black
    ),
    Page(
        title = "Test 2",
        description = "This is a test!",
        R.drawable.podcasts_black
    ),
    Page(
        title = "Test 3",
        description = "This is a test!",
        R.drawable.podcasts_black
    ),
    Page(
        title = "Test 4",
        description = "This is a test!",
        R.drawable.podcasts_black
    ),
    Page(
        title = "Test 5",
        description = "This is a test!",
        R.drawable.podcasts_black
    ),
)