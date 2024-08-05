package de.devbeyer.podcast_sponsorskipper.ui.settings

sealed class SettingsEvent {
    object ToggleDownloadEpisodeImages : SettingsEvent()
}