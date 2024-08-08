package de.devbeyer.podcast_sponsorskipper.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.common.PodcastItem
import de.devbeyer.podcast_sponsorskipper.ui.common.PodcastItemLoading
import de.devbeyer.podcast_sponsorskipper.util.Constants

@Composable
fun PodcastList(
    podcastsWithRelations: LazyPagingItems<PodcastWithRelations>,
    enableMarquee: Boolean,
    onClick: (PodcastWithRelations) -> Unit,
    onEvent: (SearchEvent) -> Unit,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val hasNoError = handelPagingResults(podcastsWithRelations)
    if (hasNoError) {
        if (podcastsWithRelations.itemCount == 0) {
            Text(
                text = "No Results Found",
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
            Text(
                text = "It looks like we couldn't find any podcasts matching your search.\n" +
                        "\n" +
                        "If you have an RSS feed for the podcast you'd like to add, you can easily add it to our app!" +
                        "\n\n" +
                        "How to Add a Podcast by RSS Feed:\n" +
                        "\n" +
                        "1. Copy the RSS feed URL of the podcast.\n" +
                        "2. Press the 'Add RSS Feed' button below.\n" +
                        "3. Paste the RSS feed URL into the provided field.\n" +
                        "4. Click 'add' to start listening!",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
            OutlinedButton(
                onClick = { onEvent(SearchEvent.SearchOnline) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Constants.Dimensions.MEDIUM),
            ) {
                Text(
                    text = "Search Online",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(Constants.Dimensions.EXTRA_SMALL),
            ) {

                items(count = podcastsWithRelations.itemCount) {
                    podcastsWithRelations[it]?.let { podcastWithRelations ->
                        PodcastItem(
                            podcastWithRelations = podcastWithRelations,
                            enableMarquee = enableMarquee,
                        ) {
                            onClick(podcastWithRelations)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun handelPagingResults(podcastsWithRelations: LazyPagingItems<PodcastWithRelations>): Boolean {
    val loadState = podcastsWithRelations.loadState
    val error = when {
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        else -> null
    }

    return when {
        loadState.refresh is LoadState.Loading -> {
            LoadingPodcastList()
            false
        }

        error != null -> {
            Text(
                text = "An error occurred!",
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
            false
        }

        else -> true
    }
}

@Composable
private fun LoadingPodcastList() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Constants.Dimensions.SMALL),
    ) {
        repeat(7) {
            PodcastItemLoading()
        }
    }
}
