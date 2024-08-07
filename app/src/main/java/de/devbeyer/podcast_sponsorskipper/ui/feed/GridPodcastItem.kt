package de.devbeyer.podcast_sponsorskipper.ui.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.common.CoverImage
import de.devbeyer.podcast_sponsorskipper.util.Constants

@Composable
fun GridPodcastItem(
    triple: Triple<PodcastWithRelations?, PodcastWithRelations?, PodcastWithRelations?>,
    navigateToEpisodes: (PodcastWithRelations) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Constants.Dimensions.EXTRA_SMALL),
    ) {
        triple.first?.let {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                    )
                    .clickable { navigateToEpisodes(it) },
                contentAlignment = Alignment.Center,
            ) {
                CoverImage(context = LocalContext.current, imagePath = it.podcast.imagePath)
            }
        }
        triple.second?.let {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                    )
                    .clickable { navigateToEpisodes(it) },
                contentAlignment = Alignment.Center,
            ) {
                CoverImage(context = LocalContext.current, imagePath = it.podcast.imagePath)
            }
        } ?: Spacer(modifier = Modifier.weight(1f))
        triple.third?.let {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                    )
                    .clickable { navigateToEpisodes(it) },
                contentAlignment = Alignment.Center,
            ) {
                CoverImage(context = LocalContext.current, imagePath = it.podcast.imagePath)
            }
        } ?: Spacer(modifier = Modifier.weight(1f))
    }
}