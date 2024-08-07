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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.common.CoverImage
import de.devbeyer.podcast_sponsorskipper.ui.common.rotationEffect
import de.devbeyer.podcast_sponsorskipper.ui.common.useMarquee
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationState
import de.devbeyer.podcast_sponsorskipper.util.Constants
import de.devbeyer.podcast_sponsorskipper.util.formatDateByDistance
import de.devbeyer.podcast_sponsorskipper.util.formatDuration

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpisodeItem(
    episode: Episode,
    isDownloading: Boolean,
    state: EpisodesState,
    podcastWithRelations: PodcastWithRelations,
    navigationState: NavigationState,
    context: Context,
    onEvent: (EpisodesEvent) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit,
    navigateToEpisode: (Episode, PodcastWithRelations) -> Unit,
) {
    val episodePath = episode.episodePath
    val haptics = LocalHapticFeedback.current

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = {
                    haptics.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)

                    onEvent(
                        EpisodesEvent.OpenMenu(
                            selectedEpisode = episode,
                            menuOffset = DpOffset(32.dp, 0.dp),
                        )
                    )
                },
                onClick = {
                    navigateToEpisode(episode, podcastWithRelations)
                },
                onLongClickLabel = "Open context menu"
            )
            .padding(Constants.Dimensions.SMALL),
        /*
        .pointerInput(true) {
            detectTapGestures (
                onLongPress = { offset ->
                    val offsetX = with(density) { offset.x.toDp() }
                    val offsetY = with(density) { -offset.y.toDp() }

                    onEvent(
                        EpisodesEvent.OpenMenu(
                            selectedEpisode = episode,
                            menuOffset = DpOffset(offsetX, offsetY),
                        )
                    )
                }
            )
        }
         */
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .height(Constants.Dimensions.EXTRA_LARGE)
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
                .padding(start = Constants.Dimensions.SMALL, end = Constants.Dimensions.SMALL)
                .weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = episode.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.useMarquee(navigationState.settings.enableMarquee),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = episode.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = formatDateByDistance(episode.pubDate),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
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
                        onNavigationEvent(
                            NavigationEvent.PlayEpisode(
                                episode,
                                podcastWithRelations
                            )
                        )
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
            Text(
                text = formatDuration(episode.duration),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        val expanded =
            state.isMenuExpanded && state.selectedEpisode?.episodeUrl == episode.episodeUrl
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onEvent(EpisodesEvent.DismissMenu) },
            offset = state.menuOffset
        ) {
            if (episode.episodePath != null) {
                DropdownMenuItem(
                    text = {
                        Text(text = "Delete Episode")
                    },
                    onClick = { onEvent(EpisodesEvent.DeleteEpisode(episode)) }
                )
            }
            DropdownMenuItem(
                text = {
                    Text(text = "Mark as " + if (episode.isCompleted) "Incomplete" else "Complete")
                },
                onClick = { onEvent(EpisodesEvent.CompleteEpisode(episode)) }
            )

            if (!episode.isCompleted) {
                DropdownMenuItem(
                    text = {
                        Text(text = "Mark as Complete from here")
                    },
                    onClick = { onEvent(EpisodesEvent.CompleteEpisodesFromHere(episode)) }
                )
            }

            DropdownMenuItem(
                text = {
                    Text(text = if (episode.favorite) "Unfavorite" else "Favorite")
                },
                onClick = {
                    onEvent(EpisodesEvent.Favorite(episode = episode, favorite = !episode.favorite))
                }
            )
        }
    }
}