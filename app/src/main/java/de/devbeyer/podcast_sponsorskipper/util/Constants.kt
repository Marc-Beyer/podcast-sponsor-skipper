package de.devbeyer.podcast_sponsorskipper.util

import androidx.compose.ui.unit.dp

object Constants {
    const val DOWNLOAD_CHANNEL_ID = "DOWNLOAD_CHANNEL_ID"
    const val UPDATE_CHANNEL_ID = "UPDATE_CHANNEL_ID"
    const val DOWNLOAD_EPISODE_NOTIFICATION_ID: Int = 42
    const val UPDATE_NOTIFICATION_ID: Int = 53
    const val USERNAME = "username"
    const val TOKEN = "token"
    const val USER_PREFERENCES_NAME = "user_preferences"
    const val COMPLETED_GUIDED_TOUR = "completed_guided_tour"
    const val API_URL = "https://test.dev-beyer.de/api/v1/"
    const val COMMAND_SCHEDULE_EVENT = "SCHEDULE_EVENT"
    const val PREVIEW_LEAD_TIME = 3000
    const val PREVIEW_POST_TIME = 4000

    object Dimensions{
        val EXTRA_LARGE = 80.dp
        val LARGE = 32.dp
        val MEDIUM = 16.dp
        val SMALL_MEDIUM = 12.dp
        val SMALL = 8.dp
        val EXTRA_SMALL = 4.dp
    }
}