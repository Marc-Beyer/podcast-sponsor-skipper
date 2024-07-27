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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.common.CoverImage
import de.devbeyer.podcast_sponsorskipper.ui.common.rotationEffect
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.util.Constants
import de.devbeyer.podcast_sponsorskipper.util.formatDateByDistance

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpisodeItem(
    episode: Episode,
    podcastWithRelations: PodcastWithRelations,
    context: Context,
    onEvent: (EpisodesEvent) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit,
) {
    var isDownloading by rememberSaveable {
        mutableStateOf(false)
    }

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
        val episodePath = episode.episodePath
        if (episodePath == null) {
            if (isDownloading) {
                TextButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.Sync,
                        contentDescription = "Episode",
                        modifier = Modifier
                            .padding(8.dp)
                            .rotationEffect(),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            } else {
                TextButton(onClick = {
                    isDownloading = true
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