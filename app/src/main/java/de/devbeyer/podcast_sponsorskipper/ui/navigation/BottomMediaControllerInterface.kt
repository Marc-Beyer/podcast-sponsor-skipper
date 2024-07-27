package de.devbeyer.podcast_sponsorskipper.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.devbeyer.podcast_sponsorskipper.ui.common.CoverImage
import de.devbeyer.podcast_sponsorskipper.util.Constants
import de.devbeyer.podcast_sponsorskipper.util.formatMillisecondsToTime

@Composable
fun BottomMediaControllerInterface(
    state: NavigationState,
    onEvent: (NavigationEvent) -> Unit
) {
    val context = LocalContext.current

    if (state.selectedEpisode != null && state.selectedPodcast != null) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .padding(16.dp)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .aspectRatio(1f)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    CoverImage(
                        context = context,
                        imagePath = state.selectedEpisode.imagePath
                            ?: state.selectedEpisode.imageUrl
                    )
                }
                Spacer(modifier = Modifier.width(Constants.Dimensions.MEDIUM))
                Column {
                    Text(
                        text = state.selectedEpisode.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = state.selectedPodcast.podcast.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            Slider(
                value = if(state.currentPosition < state.duration) state.currentPosition.toFloat() else 0f,
                onValueChange = {  },
                valueRange = 0f..if(state.duration.toFloat() < 0f) 100f else state.duration.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "${formatMillisecondsToTime(state.currentPosition)} / ${formatMillisecondsToTime(state.duration)}",
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))
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
                        Icon(imageVector = Icons.Filled.Pause, contentDescription = null)
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
                            contentDescription = null
                        )
                    }
                }
                IconButton(onClick = { onEvent(NavigationEvent.SkipForward) }) {
                    Icon(imageVector = Icons.Filled.SkipNext, contentDescription = null)
                }
            }
        }
    }
}