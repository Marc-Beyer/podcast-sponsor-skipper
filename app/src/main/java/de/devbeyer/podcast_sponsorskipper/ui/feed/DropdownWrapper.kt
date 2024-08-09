package de.devbeyer.podcast_sponsorskipper.ui.feed

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.DpOffset
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.util.openLink

@Composable
fun DropdownWrapper(
    podcastWithRelations: PodcastWithRelations?,
    state: FeedState,
    onEvent: (FeedEvent) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit,
    offset: DpOffset = DpOffset.Zero,
    contents: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

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

        DropdownMenuItem(
            text = {
                Text(text = "Copy RSS Feed")
            },
            onClick = {
                podcastWithRelations?.podcast?.let {
                    clipboardManager.setText(AnnotatedString((it.url)))
                }
            }
        )

        DropdownMenuItem(
            text = {
                Text(text = "Visit Website")
            },
            onClick = {
                podcastWithRelations?.podcast?.let {
                    context.openLink(it.link)
                }
            }
        )
    }
}