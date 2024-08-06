package de.devbeyer.podcast_sponsorskipper.ui.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.common.PodcastItem
import de.devbeyer.podcast_sponsorskipper.ui.common.RefreshColumn
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavigationState
import de.devbeyer.podcast_sponsorskipper.util.Constants

@Composable
fun FeedView(
    state: FeedState,
    navigationState: NavigationState,
    onNavigationEvent: (NavigationEvent) -> Unit,
    navigateToEpisodes: (PodcastWithRelations) -> Unit,
    navigateToSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.podcastsWithRelations.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Constants.Dimensions.MEDIUM),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "You haven't subscribed to any podcasts yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = Constants.Dimensions.MEDIUM)
            )
            Button(onClick = { navigateToSearch() }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(Constants.Dimensions.SMALL))
                Text(text = "Find Podcasts")
            }
        }
    } else {
        RefreshColumn(
            items = state.podcastsWithRelations,
            isRefreshing = navigationState.activeUpdateUrls.isNotEmpty(),
            onRefresh = { onNavigationEvent(NavigationEvent.UpdatePodcasts) },
            modifier = modifier,
        ) {
            PodcastItem(podcastWithRelations = it) {
                navigateToEpisodes(it)
            }
        }
    }
}