package de.devbeyer.podcast_sponsorskipper.ui.navigation.playbackController

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Square
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavigationState

@Composable
fun ControllerButtons(
    onEvent: (NavigationEvent) -> Unit,
    state: NavigationState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        IconButton(onClick = { onEvent(NavigationEvent.SkipBack) }) {
            Icon(imageVector = Icons.Filled.SkipPrevious, contentDescription = null)
        }
        if (state.isPlaying) {
            IconButton(
                onClick = { onEvent(NavigationEvent.Stop) },
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
            ) {
                Icon(
                    imageVector = Icons.Filled.Pause,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        } else {
            IconButton(
                onClick = { onEvent(NavigationEvent.Play) },
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        IconButton(onClick = { onEvent(NavigationEvent.SkipForward) }) {
            Icon(imageVector = Icons.Filled.SkipNext, contentDescription = null)
        }
        if (state.sponsorSectionStart == null || state.sponsorSectionEnd != null) {
            IconButton(
                modifier = Modifier
                    .size(64.dp)
                    .border(
                        BorderStroke(4.dp, MaterialTheme.colorScheme.error),
                        CircleShape
                    ),
                onClick = { onEvent(NavigationEvent.StartSponsorSection) }
            ) {
                Text(
                    text = "AD",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        } else {
            IconButton(
                modifier = Modifier
                    .size(64.dp)
                    .border(
                        BorderStroke(4.dp, MaterialTheme.colorScheme.error),
                        CircleShape
                    ),
                onClick = { onEvent(NavigationEvent.EndSponsorSection) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Square,
                    contentDescription = "End Sponsor Section"
                )
            }
        }
    }
}