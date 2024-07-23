package de.devbeyer.podcast_sponsorskipper.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import de.devbeyer.podcast_sponsorskipper.domain.models.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.models.PodcastWithRelations

@Composable
fun PodcastList(
    podcastsWithRelations: LazyPagingItems<PodcastWithRelations>,
    onClick: (PodcastWithRelations) -> Unit,
    modifier: Modifier,
) {
    val hasNoError = handelPagingResults(podcastsWithRelations)
    if (hasNoError) {
        if (podcastsWithRelations.itemCount == 0) {
            Text(
                text = "No Podcasts found!",
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        } else {
            LazyColumn(modifier = modifier) {

                items(count = podcastsWithRelations.itemCount) {
                    podcastsWithRelations[it]?.let { podcastWithRelations ->
                        PodcastItem(podcastWithRelations = podcastWithRelations) {
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
            .fillMaxSize()
    ) {
        repeat(7) {
            PodcastItemLoading()
        }
    }
}
