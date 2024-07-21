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

@Composable
fun PodcastList(
    podcasts: LazyPagingItems<Podcast>,
    onClick: (Podcast) -> Unit,
    modifier: Modifier,
) {
    val hasNoError = handelPagingResults(podcasts)
    if (hasNoError) {
        if (podcasts.itemCount == 0) {
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

                items(count = podcasts.itemCount) {
                    podcasts[it]?.let { podcast ->
                        PodcastItem(podcast = podcast) {
                            onClick(podcast)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun handelPagingResults(podcasts: LazyPagingItems<Podcast>): Boolean {
    val loadState = podcasts.loadState
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
