package de.devbeyer.podcast_sponsorskipper.ui.settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import de.devbeyer.podcast_sponsorskipper.domain.SettingKey
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationState

@Composable
fun SettingsView(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    navigationState: NavigationState,
    onNavigationEvent: (NavigationEvent) -> Unit,
) {
    LazyColumn {
        item {
            SettingsBooleanItem(
                text = "Download Episode Images",
                checked = navigationState.settings.downloadImages,
                onCheckedChange = {
                    onNavigationEvent(NavigationEvent.ChangeBooleanSettings(
                        settingKey = SettingKey.DOWNLOAD_IMAGES,
                        value = it,
                    ))
                }
            )
        }
    }
}