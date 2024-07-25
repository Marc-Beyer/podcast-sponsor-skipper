package de.devbeyer.podcast_sponsorskipper.ui.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.common.PodcastItem
import de.devbeyer.podcast_sponsorskipper.util.Constants

@Composable
fun FeedView(
    state: FeedState,
    navigateToEpisodes: (PodcastWithRelations) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Constants.Dimensions.SMALL),
    ) {
        items(items = state.podcastsWithRelations) {
            PodcastItem(podcastWithRelations = it) {
                navigateToEpisodes(it)
            }
        }
    }
}