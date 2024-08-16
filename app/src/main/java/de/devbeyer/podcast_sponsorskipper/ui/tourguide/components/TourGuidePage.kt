package de.devbeyer.podcast_sponsorskipper.ui.tourguide.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import de.devbeyer.podcast_sponsorskipper.ui.Dimensions
import de.devbeyer.podcast_sponsorskipper.ui.theme.PodcastSponsorSkipperTheme
import de.devbeyer.podcast_sponsorskipper.ui.tourguide.Page
import de.devbeyer.podcast_sponsorskipper.ui.tourguide.pages
import de.devbeyer.podcast_sponsorskipper.util.Constants

@Composable
fun TourGuidePage(
    modifier: Modifier = Modifier,
    page: Page,
) {
    val isDarkTheme = isSystemInDarkTheme()

    Column(
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(id = if (isDarkTheme) page.imageDark else page.imageLight),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.6f),
        )
        Spacer(modifier = Modifier.height(Constants.Dimensions.MEDIUM))
        LazyColumn {
            item {
                Text(
                    text = page.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = Dimensions.large),
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.height(Constants.Dimensions.SMALL))
                Text(
                    text = page.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = Dimensions.large),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TourGuidePagePreviewDark() {
    PodcastSponsorSkipperTheme {
        Surface {
            TourGuidePage(page = pages[1])
        }
    }
}