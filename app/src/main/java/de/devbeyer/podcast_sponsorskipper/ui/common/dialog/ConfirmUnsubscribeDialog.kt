package de.devbeyer.podcast_sponsorskipper.ui.common.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.devbeyer.podcast_sponsorskipper.ui.common.CustomDialog
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationState
import de.devbeyer.podcast_sponsorskipper.util.Constants

@Composable
fun ConfirmUnsubscribeDialog(
    state: NavigationState,
    onEvent: (NavigationEvent) -> Unit,
) {
    if (state.selectedPodcastForUnsubscribe != null) {
        CustomDialog(
            title = "Unsubscribe",
            acceptButtonText = "Unsubscribe and Delete",
            dismissButtonText = "Cancel",
            onDismissRequest = { onEvent(NavigationEvent.Unsubscribe(podcastWithRelations = null)) },
            onAcceptRequest = { onEvent(NavigationEvent.ConfirmUnsubscribe) },
        ) {
            Text(text = "Are you sure you want to unsubscribe from this podcast?")
            Spacer(modifier = Modifier.height(Constants.Dimensions.MEDIUM))
            Text(text = "This action will also delete all downloaded episodes associated with this podcast.")
        }
    }
}