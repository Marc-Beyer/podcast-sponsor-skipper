package de.devbeyer.podcast_sponsorskipper.ui.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.util.Constants

@Composable
fun GridPodcastRow(
    triple: Triple<PodcastWithRelations?, PodcastWithRelations?, PodcastWithRelations?>,
    navigateToEpisodes: (PodcastWithRelations) -> Unit,
    state: FeedState,
    onEvent: (FeedEvent) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthPx = configuration.screenWidthDp
    val screenWidthDp = with(density) { screenWidthPx.toDp() }

    Row(
        horizontalArrangement = Arrangement.spacedBy(Constants.Dimensions.EXTRA_SMALL),
    ) {
        GridPodcastItem(
            podcastWithRelations = triple.first,
            navigateToEpisodes = navigateToEpisodes,
            state = state,
            onEvent = onEvent,
            onNavigationEvent = onNavigationEvent,
            offset = DpOffset.Zero,
            modifier = Modifier.weight(1f),
        )
        GridPodcastItem(
            podcastWithRelations = triple.second,
            navigateToEpisodes = navigateToEpisodes,
            state = state,
            onEvent = onEvent,
            onNavigationEvent = onNavigationEvent,
            offset = DpOffset(screenWidthDp, 0.dp),
            modifier = Modifier.weight(1f),
        )
        GridPodcastItem(
            podcastWithRelations = triple.third,
            navigateToEpisodes = navigateToEpisodes,
            state = state,
            onEvent = onEvent,
            onNavigationEvent = onNavigationEvent,
            offset = DpOffset(screenWidthDp * 2, 0.dp),
            modifier = Modifier.weight(1f),
        )
    }
}