package de.devbeyer.podcast_sponsorskipper.ui.tourguide.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import de.devbeyer.podcast_sponsorskipper.ui.Dimensions
import de.devbeyer.podcast_sponsorskipper.ui.tourguide.Page
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
            painter = painterResource(id = if(isDarkTheme) page.imageDark else page.imageLight),
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
        Spacer(modifier = Modifier.height(Constants.Dimensions.SMALL))
        Text(
            text = page.description,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = Dimensions.large),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}