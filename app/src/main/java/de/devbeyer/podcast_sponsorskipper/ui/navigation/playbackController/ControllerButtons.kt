package de.devbeyer.podcast_sponsorskipper.ui.navigation.playbackController

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Square
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.devbeyer.podcast_sponsorskipper.R
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationState
import de.devbeyer.podcast_sponsorskipper.util.Constants

@Composable
fun ControllerButtons(
    onEvent: (NavigationEvent) -> Unit,
    state: NavigationState,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        DropdownMenu(
            expanded = state.isSpeedOpen,
            onDismissRequest = { onEvent(NavigationEvent.OpenChangePlaybackSpeed) }
        ) {
            DropdownMenuItem(
                onClick = {
                    onEvent(NavigationEvent.ChangePlaybackSpeed(0.5f))
                },
                text = { Text(text = "0.5x") }
            )
            DropdownMenuItem(
                onClick = {
                    onEvent(NavigationEvent.ChangePlaybackSpeed(0.75f))
                },
                text = { Text(text = "0.75x") }
            )
            DropdownMenuItem(
                onClick = {
                    onEvent(NavigationEvent.ChangePlaybackSpeed(1f))
                },
                text = { Text(text = "1x") }
            )
            DropdownMenuItem(
                onClick = {
                    onEvent(NavigationEvent.ChangePlaybackSpeed(1.25f))
                },
                text = { Text(text = "1.25x") }
            )
            DropdownMenuItem(
                onClick = {
                    onEvent(NavigationEvent.ChangePlaybackSpeed(1.5f))
                },
                text = { Text(text = "1.5x") }
            )
            DropdownMenuItem(
                onClick = {
                    onEvent(NavigationEvent.ChangePlaybackSpeed(1.75f))
                },
                text = { Text(text = "1.75x") }
            )
            DropdownMenuItem(
                onClick = {
                    onEvent(NavigationEvent.ChangePlaybackSpeed(2f))
                },
                text = { Text(text = "2x") }
            )
        }
        IconButton(
            onClick = { onEvent(NavigationEvent.OpenChangePlaybackSpeed) },
            modifier = Modifier.size(68.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AvTimer,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
        IconButton(
            onClick = { onEvent(NavigationEvent.SkipBack) },
            modifier = Modifier.padding(Constants.Dimensions.MEDIUM),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.rewind_media),
                contentDescription = "rewind",
                modifier = Modifier.size(Constants.Dimensions.EXTRA_LARGE)
            )
            Text(
                text = if (state.settings.rewindTime in 1..99) {
                    state.settings.rewindTime.toString()
                } else {
                    ""
                },
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = Constants.Dimensions.EXTRA_SMALL)
            )
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
        IconButton(
            modifier = Modifier.padding(Constants.Dimensions.MEDIUM),
            onClick = { onEvent(NavigationEvent.SkipForward) },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.forward_media),
                contentDescription = "rewind",
                modifier = Modifier.size(Constants.Dimensions.EXTRA_LARGE)
            )
            Text(
                text = if (state.settings.forwardTime in 1..99) {
                    state.settings.forwardTime.toString()
                } else {
                    ""
                },
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = Constants.Dimensions.EXTRA_SMALL)
            )
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