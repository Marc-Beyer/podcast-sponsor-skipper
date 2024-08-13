package de.devbeyer.podcast_sponsorskipper.ui.settings

data class SettingsState(
    val forwardTimeInputValue: String = "",
    val rewindTimeInputValue: String = "",
    val coverImageSizeInputValue: String = "",
    val showUserInfo: Boolean = false,

    val username: String = "",
    val token: String = "",
)
