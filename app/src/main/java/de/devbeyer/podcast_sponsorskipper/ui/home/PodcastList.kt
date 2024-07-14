package de.devbeyer.podcast_sponsorskipper.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import de.devbeyer.podcast_sponsorskipper.domain.models.Podcast

@Composable
fun PodcastList(
    podcasts: LazyPagingItems<Podcast>,
    onClick: (Podcast) -> Unit,
    modifier: Modifier,
) {
    val hasNoError = HandelPagingResults(podcasts)
    if(hasNoError){
        LazyColumn(modifier = modifier) {
            items(count = podcasts.itemCount){
                podcasts[it]?.let { podcast ->
                    PodcastItem(podcast = podcast) {
                        onClick(podcast)
                    }
                }
            }
        }
    }
}

@Composable
fun HandelPagingResults(podcasts: LazyPagingItems<Podcast>): Boolean {
    val loadState = podcasts.loadState
    val error = when {
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        else -> null
    }

    return when{
        loadState.refresh is LoadState.Loading -> {
            LoadingPodcastList()
            false
        }
        error != null ->{
            Text(text = "An error occurred!" + error.toString(), color = MaterialTheme.colorScheme.onBackground)
            false
        }
        else -> true
    }
}

@Composable
private fun LoadingPodcastList() {
    Column {
        repeat(10){
            PodcastItemLoading()
        }
    }
}
