package de.devbeyer.podcast_sponsorskipper.ui.tourguide.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import de.devbeyer.podcast_sponsorskipper.ui.Dimensions
import de.devbeyer.podcast_sponsorskipper.ui.tourguide.Page

@Composable
fun TourGuidePage(
    modifier: Modifier = Modifier,
    page: Page,
) {
    Column(
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(id = page.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.6f),
        )
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(horizontal = Dimensions.large),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = page.description,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = Dimensions.large),
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}