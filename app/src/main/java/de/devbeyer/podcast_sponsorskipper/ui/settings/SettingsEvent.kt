package de.devbeyer.podcast_sponsorskipper.ui.settings

sealed class SettingsEvent {
    data class ChangeForwardTimeInputValue(val value: String) : SettingsEvent()
    data class ChangeRewindTimeInputValue(val value: String) : SettingsEvent()
    data class ChangeCoverImageSizeInputValue(val value: String) : SettingsEvent()
    data class ResetSettings(val callback: () -> Unit) : SettingsEvent()

    object UpdateSettings : SettingsEvent()
}