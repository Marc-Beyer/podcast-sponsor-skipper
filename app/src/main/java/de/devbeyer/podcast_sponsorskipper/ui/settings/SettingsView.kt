package de.devbeyer.podcast_sponsorskipper.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
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
            SettingsBooleanItem(
                text = "Feed Grid Layout",
                checked = navigationState.settings.feedGridLayout,
                onCheckedChange = {
                    onNavigationEvent(NavigationEvent.ChangeBooleanSettings(
                        settingKey = SettingKey.FEED_GRID_LAYOUT,
                        value = it,
                    ))
                }
            )
        }
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