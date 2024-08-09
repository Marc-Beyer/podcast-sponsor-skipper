package de.devbeyer.podcast_sponsorskipper.ui.feed

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpOffset
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationEvent

@Composable
fun DropdownWrapper(
    podcastWithRelations: PodcastWithRelations?,
    state: FeedState,
    onEvent: (FeedEvent) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit,
    offset: DpOffset = DpOffset.Zero,
    contents: @Composable () -> Unit,
) {
    contents()

    val expanded =
        state.isMenuExpanded && state.selectedPodcast?.podcast?.id == podcastWithRelations?.podcast?.id
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onEvent(FeedEvent.DismissMenu) },
        offset = offset,
    ) {
        DropdownMenuItem(
            text = {
                Text(text = "Update")
            },
            onClick = {
                podcastWithRelations?.podcast?.let { podcast ->
                    onNavigationEvent(
                        NavigationEvent.UpdatePodcast(
                            podcast = podcast,
                        )
                    )
                }
            }
        )

        DropdownMenuItem(
            text = {
                Text(text = "Unsubscribe")
            },
            onClick = {
                onNavigationEvent(
                    NavigationEvent.Unsubscribe(
                        podcastWithRelations = podcastWithRelations,
                    )
                )
            }
        )
    }
}