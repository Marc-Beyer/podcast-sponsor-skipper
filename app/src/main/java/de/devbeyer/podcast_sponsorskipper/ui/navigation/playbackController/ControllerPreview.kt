package de.devbeyer.podcast_sponsorskipper.ui.navigation.playbackController

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.devbeyer.podcast_sponsorskipper.ui.common.rotationEffect
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavigationState
import de.devbeyer.podcast_sponsorskipper.ui.navigation.PreviewState
import de.devbeyer.podcast_sponsorskipper.util.Constants
import de.devbeyer.podcast_sponsorskipper.util.formatMillisecondsToTime

@Composable
fun ControllerPreview(
    state: NavigationState,
    onEvent: (NavigationEvent) -> Unit
) {
    Spacer(modifier = Modifier.height(Constants.Dimensions.MEDIUM))
    Text(
        text = "Sponsor section",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(Constants.Dimensions.SMALL))
    Text(
        text = "Start: ${formatMillisecondsToTime(state.sponsorSectionStart ?: 0)}",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = "End: ${formatMillisecondsToTime(state.sponsorSectionEnd ?: 0)}",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(Constants.Dimensions.MEDIUM))

    when (state.isPreviewing) {
        PreviewState.NONE -> {
            Button(
                onClick = { onEvent(NavigationEvent.Preview) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
            ) {
                Text(
                    text = "Preview the segment",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        PreviewState.PREVIEWING -> {
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Sync,
                    contentDescription = "Episode",
                    modifier = Modifier
                        .padding(1.dp)
                        .rotationEffect(),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        PreviewState.FINISHED -> {
            Button(
                onClick = { onEvent(NavigationEvent.Preview) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
            ) {
                Text(
                    text = "Preview the segment again",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(Constants.Dimensions.SMALL))
            OutlinedButton(
                onClick = { onEvent(NavigationEvent.DiscardSegment) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
            ) {
                Text(
                    text = "Discard Segment",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(Constants.Dimensions.SMALL))
            OutlinedButton(
                onClick = { onEvent(NavigationEvent.SubmitSegment) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
            ) {
                Text(
                    text = "Submit the segment",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(Constants.Dimensions.MEDIUM))
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = Icons.Outlined.Info, contentDescription = null)
        Spacer(modifier = Modifier.width(Constants.Dimensions.MEDIUM))
        Text(
            text = "Please review the highlighted sections before submitting your sponsor segment.",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}