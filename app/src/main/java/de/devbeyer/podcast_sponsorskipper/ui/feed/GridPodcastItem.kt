package de.devbeyer.podcast_sponsorskipper.ui.feed

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.DpOffset
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.common.CoverImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GridPodcastItem(
    podcastWithRelations: PodcastWithRelations?,
    navigateToEpisodes: (PodcastWithRelations) -> Unit,
    state: FeedState,
    onEvent: (FeedEvent) -> Unit,
    offset: DpOffset,
    modifier: Modifier = Modifier,
) {
    val haptics = LocalHapticFeedback.current

    podcastWithRelations?.let {
        DropdownWrapper(
            podcastWithRelations = podcastWithRelations,
            state = state,
            onEvent = onEvent,
            offset = offset,
        ) {
            Box(
                modifier = modifier
                    .aspectRatio(1f)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                    )
                    .combinedClickable(
                        onLongClick = {
                            haptics.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)

                            onEvent(
                                FeedEvent.OpenMenu(
                                    selectedPodcast = podcastWithRelations,
                                    menuOffset = DpOffset.Zero,
                                )
                            )
                        },
                        onClick = {
                            navigateToEpisodes(it)
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                CoverImage(context = LocalContext.current, imagePath = it.podcast.imagePath)
            }
        }
    } ?: Spacer(modifier = modifier)
}