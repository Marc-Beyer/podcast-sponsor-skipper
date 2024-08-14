package de.devbeyer.podcast_sponsorskipper.ui.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Category
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.common.CoverImage
import de.devbeyer.podcast_sponsorskipper.ui.common.rotationEffect
import de.devbeyer.podcast_sponsorskipper.ui.theme.PodcastSponsorSkipperTheme
import de.devbeyer.podcast_sponsorskipper.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoView(
    state: InfoState,
    onEvent: (InfoEvent) -> Unit,
    gotoEpisodes: (PodcastWithRelations) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Constants.Dimensions.MEDIUM),
    ) {
        item {
            if (state.podcastWithRelations != null) {
                PodcastInfo(
                    podcastWithRelations = state.podcastWithRelations,
                    isSubscribed = state.subscribedToPodcast,
                    isLoading = state.isLoading,
                    onEvent = onEvent,
                    gotoEpisodes = gotoEpisodes,
                )
            } else {
                Text(text = "An error occurred", textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun PodcastInfo(
    podcastWithRelations: PodcastWithRelations,
    isSubscribed: Boolean,
    isLoading: Boolean,
    onEvent: (InfoEvent) -> Unit,
    gotoEpisodes: (PodcastWithRelations) -> Unit,
) {
    val context = LocalContext.current
    val podcast = podcastWithRelations.podcast

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(150.dp)
                .aspectRatio(1f)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                ),
            contentAlignment = Alignment.Center,
        ) {
            val imagePath = podcast.imagePath ?: podcast.imageUrl
            CoverImage(
                context = context,
                imagePath = imagePath,
            )
        }

        Spacer(modifier = Modifier.width(Constants.Dimensions.MEDIUM))

        Column {
            if (podcast.author != null) {
                IconWithText(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Author",
                    text = podcast.author,
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            IconWithText(
                imageVector = Icons.Filled.Category,
                contentDescription = "Categories",
                text = podcastWithRelations.categories.joinToString { it.name },
            )

            Spacer(modifier = Modifier.height(Constants.Dimensions.SMALL))

            IconWithText(
                imageVector = Icons.Filled.Language,
                contentDescription = "Language",
                text = podcast.language,
            )

            Spacer(modifier = Modifier.height(Constants.Dimensions.SMALL))

            IconWithText(
                imageVector = Icons.Filled.LibraryMusic,
                contentDescription = "Nr of episodes",
                text = podcast.nrOdEpisodes.toString() + " episodes",
            )
        }
    }

    Spacer(modifier = Modifier.height(Constants.Dimensions.MEDIUM))

    if (isSubscribed) {
        OutlinedButton(
            onClick = {
                onEvent(InfoEvent.UnsubscribeFromPodcast(podcastWithRelations))
            },
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(text = "Unsubscribe", color = MaterialTheme.colorScheme.onSurface)

        }
        OutlinedButton(
            onClick = {
                gotoEpisodes(podcastWithRelations)
            },
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(text = "View Episodes", color = MaterialTheme.colorScheme.onSurface)

        }
    } else {
        Button(
            onClick = {
                onEvent(InfoEvent.SubscribeToPodcast(podcastWithRelations))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp),
        ) {
            if (isLoading) {
                Icon(
                    imageVector = Icons.Outlined.Sync,
                    contentDescription = "Episode",
                    modifier = Modifier
                        .padding(1.dp)
                        .rotationEffect(),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                Text(text = "Subscribe")
            }
        }
    }


    Spacer(modifier = Modifier.height(Constants.Dimensions.MEDIUM))

    Text(
        text = podcast.description,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
fun IconWithText(
    imageVector: ImageVector,
    contentDescription: String,
    text: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PodcastInfoPreview() {
    PodcastSponsorSkipperTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            item {
                PodcastInfo(
                    PodcastWithRelations(
                        podcast = Podcast(
                            id = 1,
                            url = "https://changelog.com/jsparty/feed",
                            title = "JS Party: JavaScript, CSS, Web Development",
                            description = "Your weekly celebration of JavaScript and the web. Panelists include Jerod Santo, Feross Aboukhadijeh, Kevin Ball, Amelia Wattenberger, Nick Nisi, Divya Sasidharan, Mikeal Rogers, Chris Hiller, and Amal Hussein. Topics discussed include the web platform (Chrome, Safari, Edge, Firefox, Brave, etc), front-end frameworks (React, Solid, Svelte, Vue, Angular, etc), JavaScript and TypeScript runtimes (Node, Deno, Bun), web animation, SVG, robotics, IoT, and much more. If JavaScript and/or the web touch your life, this show’s for you. Some people search for JSParty and can’t find the show, so now the string JSParty is in our description too.",
                            link = "https://changelog.com/jsparty",
                            language = "en-us",
                            imageUrl = "https://cdn.changelog.com/uploads/covers/js-party-original.png?v=63725770332",
                            explicit = false,
                            locked = false,
                            complete = false,
                            lastUpdate = "String",
                            nrOdEpisodes = 42,
                            copyright = "All rights reserved",
                            author = "Changelog Media",
                            fundingText = "Support our work by joining Changelog++",
                            fundingUrl = "https://changelog.com/++",
                            imagePath = null,
                        ),
                        categories = listOf(
                            Category(1, "Technology"),
                            Category(2, "Software How-To"),
                            Category(3, "Tech News")
                        ),
                    ),
                    true,
                    false,
                    onEvent = {},
                    gotoEpisodes = {},
                )
            }
        }
    }
}
