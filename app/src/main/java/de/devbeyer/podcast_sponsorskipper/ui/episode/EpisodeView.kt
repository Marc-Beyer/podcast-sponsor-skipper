package de.devbeyer.podcast_sponsorskipper.ui.episode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import de.devbeyer.podcast_sponsorskipper.ui.common.CoverImage
import de.devbeyer.podcast_sponsorskipper.ui.info.IconWithText
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavigationState
import de.devbeyer.podcast_sponsorskipper.util.Constants
import de.devbeyer.podcast_sponsorskipper.util.formatLocalDateTime
import de.devbeyer.podcast_sponsorskipper.util.formatMillisecondsToTime

@Composable
fun EpisodeView(
    state: EpisodeState,
    navigationState: NavigationState,
    onEvent: (EpisodeEvent) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit,
) {
    val context = LocalContext.current
    val episode = state.episode

    if (episode != null) {
        LazyColumn(
            modifier = Modifier.padding(Constants.Dimensions.MEDIUM),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Box(
                    modifier = Modifier
                        .padding(Constants.Dimensions.LARGE)
                        .aspectRatio(1f)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    val imagePath = episode.imagePath ?: episode.imageUrl
                    CoverImage(
                        context = context,
                        imagePath = imagePath,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconWithText(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "pubDate",
                        text = formatLocalDateTime(episode.pubDate),
                    )
                    PlayDownloadButton(
                        navigationState = navigationState,
                        state = state,
                        onEvent = onEvent,
                        onNavigationEvent = onNavigationEvent,
                        isDownloading = state.activeDownloadUrls.contains(episode.episodeUrl),
                    )
                }


                Spacer(modifier = Modifier.height(Constants.Dimensions.MEDIUM))

                Text(text = episode.description)

                Spacer(modifier = Modifier.height(Constants.Dimensions.MEDIUM))


                if(episode.episodePath != null){
                    Text(
                        text = "Sponsor Sections:",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Spacer(modifier = Modifier.height(Constants.Dimensions.SMALL))
                    if(state.sponsorSections.isEmpty()){
                        Text(text = "None yet")
                    }else{
                        for (sponsorSection in state.sponsorSections){
                            Text(text = formatMillisecondsToTime(sponsorSection.startPosition) + " - " + formatMillisecondsToTime(sponsorSection.endPosition))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlayDownloadButton(
    navigationState: NavigationState,
    state: EpisodeState,
    isDownloading: Boolean,
    onEvent: (EpisodeEvent) -> Unit,
    onNavigationEvent:(NavigationEvent) -> Unit,
){
    val episode = state.episode ?: return
    val podcastWithRelations = state.podcastWithRelations ?: return
    val episodePath = episode.episodePath

    if (episodePath == null) {
        if (isDownloading) {
            TextButton(onClick = {
                onEvent(
                    EpisodeEvent.CancelDownload(episode = episode)
                )
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Cancel download",
                    modifier = Modifier
                        .padding(8.dp),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Cancel Download",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        } else {
            TextButton(onClick = {
                onEvent(EpisodeEvent.Download(episode))
            }) {
                Icon(
                    imageVector = Icons.Filled.Download,
                    contentDescription = "Episode",
                    modifier = Modifier.padding(8.dp),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Download",
                    color = MaterialTheme.colorScheme.onSurface
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
                Text(
                    text = "Pause",
                    color = MaterialTheme.colorScheme.onSurface
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
                Text(
                    text = "Play",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}