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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import de.devbeyer.podcast_sponsorskipper.domain.models.Category
import de.devbeyer.podcast_sponsorskipper.domain.models.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.models.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.theme.PodcastSponsorSkipperTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoView(
    state: InfoState,
    onEvent: (InfoEvent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        item {
            if (state.podcastWithRelations != null) {
                PodcastInfo(state.podcastWithRelations, state.subscribedToPodcast, onEvent)
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
    onEvent: (InfoEvent) -> Unit,
) {
    val context = LocalContext.current
    val podcast = podcastWithRelations.podcast

    Text(
        text = podcast.title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(150.dp)
                .aspectRatio(1f)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                ),
            contentAlignment = Alignment.Center,
        ) {
            val imagePath = podcast.imageUrl
            AsyncImage(
                model = ImageRequest.Builder(context).data(imagePath).build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

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

            Spacer(modifier = Modifier.height(8.dp))

            IconWithText(
                imageVector = Icons.Filled.Language,
                contentDescription = "Language",
                text = podcast.language,
            )

            Spacer(modifier = Modifier.height(8.dp))

            IconWithText(
                imageVector = Icons.Filled.LibraryMusic,
                contentDescription = "Nr of episodes",
                text = podcast.nrOdEpisodes.toString() + " episodes",
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = podcast.description,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(8.dp))

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {
            onEvent(InfoEvent.subscribeToPodcast(podcastWithRelations))
        },
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(text = if (isSubscribed) "Unsubscribe" else "Subscribe")
    }
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
                        ),
                        categories = listOf(
                            Category(1, "Technology"),
                            Category(2, "Software How-To"),
                            Category(3, "Tech News")
                        ),
                    ),
                    false
                ) {}
            }
        }
    }
}
