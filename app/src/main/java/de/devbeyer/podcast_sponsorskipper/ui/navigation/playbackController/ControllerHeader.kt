package de.devbeyer.podcast_sponsorskipper.ui.navigation.playbackController

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.common.CoverImage
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.util.Constants

@Composable
fun ControllerHeader(
    navigateToEpisode: (Episode, PodcastWithRelations) -> Unit,
    context: Context,
    selectedEpisode: Episode,
    selectedPodcast: PodcastWithRelations,
    onEvent: (NavigationEvent) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .height(48.dp)
                .aspectRatio(1f)
                .clickable {
                    navigateToEpisode(
                        selectedEpisode,
                        selectedPodcast
                    )
                }
                .background(
                    color = MaterialTheme.colorScheme.primary,
                ),
            contentAlignment = Alignment.Center,
        ) {
            CoverImage(
                context = context,
                imagePath = selectedEpisode.imagePath
                    ?: selectedEpisode.imageUrl
            )
        }
        Spacer(modifier = Modifier.width(Constants.Dimensions.MEDIUM))
        Column(
            modifier = Modifier
                .weight(5f)
                .clickable {
                    navigateToEpisode(
                        selectedEpisode,
                        selectedPodcast
                    )
                },
        ) {
            Text(
                text = selectedEpisode.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = selectedPodcast.podcast.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier.weight(1f),
        ) {
            IconButton(onClick = { onEvent(NavigationEvent.Close) }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            }
        }
    }
}