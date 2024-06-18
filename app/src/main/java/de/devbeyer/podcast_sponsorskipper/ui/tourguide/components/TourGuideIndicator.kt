package de.devbeyer.podcast_sponsorskipper.ui.tourguide.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import de.devbeyer.podcast_sponsorskipper.ui.Dimensions
import de.devbeyer.podcast_sponsorskipper.ui.tourguide.pages

@Composable
fun TourGuideIndicator(
    currentPage: Int,
    nrOfPages: Int,
    indicatorSize: Dp,
    padding: Dp,
) {
    Row(
        modifier = Modifier.padding(Dimensions.large),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        repeat(times = nrOfPages) { index ->
            Box(
                modifier = Modifier
                    .size(indicatorSize)
                    .clip(CircleShape)
                    .background(
                        if (index == currentPage) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.tertiary
                        }
                    )
            )
            if (index < nrOfPages - 1) Spacer(modifier = Modifier.width(padding))
        }
    }
}