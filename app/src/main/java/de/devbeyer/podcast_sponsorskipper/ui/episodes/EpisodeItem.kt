package de.devbeyer.podcast_sponsorskipper.ui.episodes

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.common.CoverImage
import de.devbeyer.podcast_sponsorskipper.ui.common.rotationEffect
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavigationState
import de.devbeyer.podcast_sponsorskipper.util.Constants
import de.devbeyer.podcast_sponsorskipper.util.formatDateByDistance

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpisodeItem(
    episode: Episode,
    isDownloading: Boolean,
    podcastWithRelations: PodcastWithRelations,
    navigationState: NavigationState,
    context: Context,
    onEvent: (EpisodesEvent) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit,
) {
    /*
    var isDownloading by rememberSaveable {
        mutableStateOf(inDownloadQueue)
    }
     */
    val episodePath = episode.episodePath

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .combinedClickable(
                onLongClick = {

                },
                onClick = {}
            )
            .fillMaxWidth()
            .padding(Constants.Dimensions.MEDIUM),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .height(64.dp)
                .aspectRatio(1f)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                ),
            contentAlignment = Alignment.Center,
        ) {
            val imagePath = episode.imagePath ?: episode.imageUrl
            CoverImage(
                context = context,
                imagePath = imagePath
            )
            val offset = with(LocalDensity.current) { 32.dp.toPx() }
            val triangleShape = GenericShape { size, _ ->
                moveTo(size.width - offset, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, offset)
                close()
            }
            if (episode.isCompleted) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.secondary, shape = triangleShape),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .padding(2.dp),
                        imageVector = Icons.Filled.Done,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
            if (episodePath == null && isDownloading) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondary, shape = CircleShape),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Sync,
                        contentDescription = "Episode",
                        modifier = Modifier
                            .padding(8.dp)
                            .rotationEffect(),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(start = Constants.Dimensions.MEDIUM, end = Constants.Dimensions.MEDIUM)
                .weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = episode.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = episode.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = formatDateByDistance(episode.pubDate),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (episodePath == null) {
            if (isDownloading) {
                TextButton(onClick = {
                    onEvent(
                        EpisodesEvent.CancelDownload(episode = episode)
                    )
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cancel download",
                        modifier = Modifier
                            .padding(8.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            } else {
                TextButton(onClick = {
                    onEvent(EpisodesEvent.Download(episode))
                }) {
                    Icon(
                        imageVector = Icons.Filled.Download,
                        contentDescription = "Episode",
                        modifier = Modifier.padding(8.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        } else {
            if (navigationState.selectedEpisode == episode && navigationState.isPlaying) {
                TextButton(onClick = {
                    onNavigationEvent(NavigationEvent.Stop)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Pause,
                        contentDescription = "Episode",
                        modifier = Modifier.padding(8.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            } else {
                TextButton(onClick = {
                    onNavigationEvent(NavigationEvent.PlayEpisode(episode, podcastWithRelations))
                }) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Episode",
                        modifier = Modifier.padding(8.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}