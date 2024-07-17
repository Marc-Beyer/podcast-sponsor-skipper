package de.devbeyer.podcast_sponsorskipper.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import de.devbeyer.podcast_sponsorskipper.domain.models.Category
import de.devbeyer.podcast_sponsorskipper.domain.models.Podcast
import de.devbeyer.podcast_sponsorskipper.ui.theme.PodcastSponsorSkipperTheme


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PodcastItem(podcast: Podcast, onClick: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .combinedClickable(
                onLongClick = {

                },
                onClick = onClick
            )
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .height(64.dp)
                .aspectRatio(1f)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                ),
            contentAlignment = Alignment.Center,
        ) {
            val imagePath = podcast.imageUrl
            AsyncImage(
                model = ImageRequest.Builder(context).data(imagePath).build(),
                contentDescription = null,
                modifier = Modifier.size(96.dp)
            )
        }
        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = podcast.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = podcast.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun PodcastItemLoading() {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .height(64.dp)
                .width(64.dp)
                .aspectRatio(1f)
                .loadingEffect(),
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .width(120.dp)
                    .loadingEffect(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
                    .loadingEffect(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .height(16.dp)
                    .width(150.dp)
                    .loadingEffect(),
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PodcastItemPreview() {
    PodcastSponsorSkipperTheme {
        PodcastItem(
            podcast = Podcast(
                id = 1,
                url = "String",
                title = "String",
                description = "String",
                link = "String",
                language = "String",
                imageUrl = "https://cdn.changelog.com/uploads/covers/js-party-original.png?v=63725770332",
                explicit = false,
                locked = false,
                complete = false,
                lastUpdate = "String",
                nrOdEpisodes = 42,
                copyright = "",
                author = "String",
                fundingText = "String",
                fundingUrl = "",
                categories = listOf(Category(1, "Test")),
            )
        ) {

        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PodcastItemPreviewDark() {
    PodcastSponsorSkipperTheme {
        PodcastItem(
            podcast = Podcast(
                id = 1,
                url = "String",
                title = "String",
                description = "String",
                link = "String",
                language = "String",
                imageUrl = "https://cdn.changelog.com/uploads/covers/js-party-original.png?v=63725770332",
                explicit = false,
                locked = false,
                complete = false,
                lastUpdate = "String",
                nrOdEpisodes = 42,
                copyright = "",
                author = "String",
                fundingText = "String",
                fundingUrl = "",
                categories = listOf(Category(1, "Test")),
            )
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PodcastItemLoadingPreview() {
    PodcastSponsorSkipperTheme {
        PodcastItemLoading()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PodcastItemLoadingPreviewDark() {
    PodcastSponsorSkipperTheme {
        PodcastItemLoading()
    }
}