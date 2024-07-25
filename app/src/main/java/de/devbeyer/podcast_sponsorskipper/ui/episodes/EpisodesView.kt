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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Podcasts
import androidx.compose.material3.Button
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
import de.devbeyer.podcast_sponsorskipper.ui.search.SearchEvent
import de.devbeyer.podcast_sponsorskipper.util.Constants

@Composable
fun EpisodesView(
    state: EpisodesState,
    onEvent: (EpisodesEvent) -> Unit,
) {
    val context = LocalContext.current
    val podcast = state.podcastWithRelations?.podcast
    val categories = state.podcastWithRelations?.categories ?: emptyList()

    if (podcast != null) {
        Column(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Row {
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

                    if (!podcast.fundingText.isNullOrBlank()) {
                        Button(
                            onClick = {
                                //context.openLink(podcast.fundingUrl)
                            }
                        ) {
                            Text(
                                text = podcast.fundingText,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                // context.openLink(podcast.link)
                            }
                        ) {
                            Text(
                                text = "Homepage",
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                    //val categories: List<String> = categories.map { category -> category.categoryId }
                    Text(
                        text = categories.joinToString(", ") { it.name },
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            if (state.episodes.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.padding(top = Constants.Dimensions.SMALL),
                    verticalArrangement = Arrangement.spacedBy(Constants.Dimensions.SMALL),
                ) {
                    items(items = state.episodes) { episode ->
                        EpisodeItem(episode = episode, context = context, startAudio = {})
                    }
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