package de.devbeyer.podcast_sponsorskipper.ui.settings

sealed class SettingsEvent {
    data class ChangeForwardTimeInputValue(val value: String): SettingsEvent()
    data class ChangeRewindTimeInputValue(val value: String): SettingsEvent()

    object UpdateSettings: SettingsEvent()
}