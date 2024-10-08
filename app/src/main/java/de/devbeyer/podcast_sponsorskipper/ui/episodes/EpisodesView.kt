package de.devbeyer.podcast_sponsorskipper.ui.episodes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Podcasts
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.common.DropDown
import de.devbeyer.podcast_sponsorskipper.ui.common.RefreshColumn
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationState
import de.devbeyer.podcast_sponsorskipper.util.Constants
import de.devbeyer.podcast_sponsorskipper.util.isNotOlderThanAWeek
import de.devbeyer.podcast_sponsorskipper.util.openLink

@Composable
fun EpisodesView(
    state: EpisodesState,
    navigationState: NavigationState,
    onEvent: (EpisodesEvent) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit,
    navigateToEpisode: (Episode, PodcastWithRelations) -> Unit,
) {
    val context = LocalContext.current
    val podcast = state.podcastWithRelations?.podcast
    val categories = state.podcastWithRelations?.categories ?: emptyList()
    val episodes = state.episodes.filter {
        when (state.activeFilter) {
            EpisodeFilter.ALL -> true
            EpisodeFilter.DOWNLOADED -> it.episodePath != null
            EpisodeFilter.INCOMPLETE -> !it.isCompleted
            EpisodeFilter.RECENT -> isNotOlderThanAWeek(it.pubDate)
            EpisodeFilter.FAVORITE -> it.favorite
        }
    }

    if (podcast != null) {
        Column(
            modifier = Modifier.padding( top = Constants.Dimensions.SMALL),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(modifier = Modifier.padding(start = Constants.Dimensions.SMALL, end = Constants.Dimensions.SMALL)) {
                Box(
                    modifier = Modifier
                        .height(98.dp)
                        .aspectRatio(1f)
                        .background(
                            shape = RoundedCornerShape(100f),
                            color = MaterialTheme.colorScheme.primary,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    val imagePath = podcast.imagePath ?: podcast.imageUrl
                    if (imagePath.isNotBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(imagePath).build(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Podcasts,
                            contentDescription = "Podcasts",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.Center,
                ) {

                    Text(
                        text = podcast.author ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            context.openLink(
                                url = if (podcast.fundingUrl.isNullOrBlank()) {
                                    podcast.link
                                } else {
                                    podcast.fundingUrl
                                }
                            )
                        }
                    ) {
                        Text(
                            text = if (podcast.fundingText.isNullOrBlank()) {
                                "Homepage"
                            } else {
                                podcast.fundingText
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                        )
                    }

                    Text(
                        text = categories.joinToString(", ") { it.name },
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            DropDown(
                text = when (state.activeFilter) {
                    EpisodeFilter.ALL -> "All Episodes (${episodes.size})"
                    EpisodeFilter.DOWNLOADED -> "Downloaded Episodes (${episodes.size})"
                    EpisodeFilter.INCOMPLETE -> "Incomplete Episodes (${episodes.size})"
                    EpisodeFilter.RECENT -> "Recent Episodes (${episodes.size})"
                    EpisodeFilter.FAVORITE -> "Favorite Episodes (${episodes.size})"
                },
                expanded = state.isFilterMenuExpanded,
                onExpandedChanged = { onEvent(EpisodesEvent.SetFilterMenuExpanded(it)) },
                modifier = Modifier.padding(horizontal = Constants.Dimensions.SMALL)
            ) {
                DropdownMenuItem(
                    text = { Text("All Episodes") },
                    onClick = { onEvent(EpisodesEvent.SetFilter(EpisodeFilter.ALL)) }
                )
                DropdownMenuItem(
                    text = { Text("Downloaded Episodes") },
                    onClick = { onEvent(EpisodesEvent.SetFilter(EpisodeFilter.DOWNLOADED)) }
                )
                DropdownMenuItem(
                    text = { Text("Incomplete Episodes") },
                    onClick = { onEvent(EpisodesEvent.SetFilter(EpisodeFilter.INCOMPLETE)) }
                )
                DropdownMenuItem(
                    text = { Text("Recent Episodes") },
                    onClick = { onEvent(EpisodesEvent.SetFilter(EpisodeFilter.RECENT)) }
                )
                DropdownMenuItem(
                    text = { Text("Favorite Episodes") },
                    onClick = { onEvent(EpisodesEvent.SetFilter(EpisodeFilter.FAVORITE)) }
                )
            }

            if (state.episodes.isNotEmpty()) {
                RefreshColumn(
                    items = episodes,
                    isRefreshing = navigationState.activeUpdateUrls.contains(podcast.url),
                    onRefresh = {
                        onNavigationEvent(NavigationEvent.UpdatePodcast(podcast = podcast))
                    },
                ) { episode ->
                    EpisodeItem(
                        episode = episode,
                        isDownloading = state.activeDownloadUrls.contains(episode.episodeUrl),
                        state = state,
                        podcastWithRelations = state.podcastWithRelations,
                        navigationState = navigationState,
                        context = context,
                        onEvent = onEvent,
                        onNavigationEvent = onNavigationEvent,
                        navigateToEpisode = navigateToEpisode,
                    )
                }

            } else {
                Text(
                    text = "No episodes",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}