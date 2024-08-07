package de.devbeyer.podcast_sponsorskipper.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.devbeyer.podcast_sponsorskipper.domain.SettingKey
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationState
import de.devbeyer.podcast_sponsorskipper.util.Constants

@Composable
fun SettingsView(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    navigationState: NavigationState,
    onNavigationEvent: (NavigationEvent) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Constants.Dimensions.EXTRA_SMALL),
    ) {
        item {
            Text(
                text = "Playback Settings",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(Constants.Dimensions.EXTRA_SMALL),
            )
        }
        item {
            SettingsBooleanItem(
                text = "Feed Grid Layout",
                description = "Enable grid layout for the feed",
                checked = navigationState.settings.feedGridLayout,
                onCheckedChange = {
                    onNavigationEvent(
                        NavigationEvent.ChangeBooleanSettings(
                            settingKey = SettingKey.FEED_GRID_LAYOUT,
                            value = it,
                        )
                    )
                }
            )
        }
        item {
            SettingsBooleanItem(
                text = "Notification Player Background",
                description = "Use the episode image as the background for the notification player",
                checked = navigationState.settings.setNotificationImage,
                onCheckedChange = {
                    onNavigationEvent(
                        NavigationEvent.ChangeBooleanSettings(
                            settingKey = SettingKey.SET_NOTIFICATION_IMAGE,
                            value = it,
                        )
                    )
                }
            )
        }
        item {
            SettingsBooleanItem(
                text = "Enable Marquee Effect",
                description = "Scroll long text horizontally to fit within the available space",
                checked = navigationState.settings.enableMarquee,
                onCheckedChange = {
                    onNavigationEvent(
                        NavigationEvent.ChangeBooleanSettings(
                            settingKey = SettingKey.ENABLE_MARQUEE,
                            value = it,
                        )
                    )
                }
            )
        }
        item {
            SettingsBooleanItem(
                text = "Enable Marquee in Player",
                description = "Enable scrolling text for long titles in the podcast player to ensure all content is visible",
                checked = navigationState.settings.enableMarqueeInPlayer,
                onCheckedChange = {
                    onNavigationEvent(
                        NavigationEvent.ChangeBooleanSettings(
                            settingKey = SettingKey.ENABLE_MARQUEE_IN_PLAYER,
                            value = it,
                        )
                    )
                }
            )
        }

        item {
            Text(
                text = "Storage and Cleanup Settings",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(Constants.Dimensions.EXTRA_SMALL),
            )
        }
        item {
            SettingsBooleanItem(
                text = "Download Episode Artwork",
                description = "Automatically download images for episodes",
                checked = navigationState.settings.downloadImages,
                onCheckedChange = {
                    onNavigationEvent(
                        NavigationEvent.ChangeBooleanSettings(
                            settingKey = SettingKey.DOWNLOAD_IMAGES,
                            value = it,
                        )
                    )
                }
            )
        }
        item {
            SettingsBooleanItem(
                text = "Auto-Delete Completed Episodes",
                description = "Automatically delete episodes after they have been played to completion",
                checked = navigationState.settings.autoDeleteCompletedEpisodes,
                onCheckedChange = {
                    onNavigationEvent(
                        NavigationEvent.ChangeBooleanSettings(
                            settingKey = SettingKey.AUTO_DELETE_COMPLETED_EPISODES,
                            value = it,
                        )
                    )
                }
            )
        }

        item {
            Text(
                text = "Playback Settings",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(Constants.Dimensions.EXTRA_SMALL),
            )
        }
        item {
            SettingsIntItem(
                text = "Forward Time Interval",
                description = "Set the time interval (in seconds) for forwarding the podcast",
                value = state.forwardTimeInputValue,
                onDone = {
                    val value = state.forwardTimeInputValue.toIntOrNull()
                    if (value != null) {
                        onNavigationEvent(
                            NavigationEvent.ChangeIntSettings(
                                settingKey = SettingKey.FORWARD_TIME,
                                value = value,
                            )
                        )
                    } else {
                        onEvent(SettingsEvent.UpdateSettings)
                    }
                },
                onValueChange = {
                    onEvent(SettingsEvent.ChangeForwardTimeInputValue(it))
                }
            )
        }
        item {
            SettingsIntItem(
                text = "Rewind Time Interval",
                description = "Set the time interval (in seconds) for rewinding the podcast",
                value = state.rewindTimeInputValue,
                onDone = {
                    val value = state.rewindTimeInputValue.toIntOrNull()
                    if (value != null) {
                        onNavigationEvent(
                            NavigationEvent.ChangeIntSettings(
                                settingKey = SettingKey.REWIND_TIME,
                                value = value,
                            )
                        )
                    } else {
                        onEvent(SettingsEvent.UpdateSettings)
                    }
                },
                onValueChange = {
                    onEvent(SettingsEvent.ChangeRewindTimeInputValue(it))
                }
            )
        }
    }
}