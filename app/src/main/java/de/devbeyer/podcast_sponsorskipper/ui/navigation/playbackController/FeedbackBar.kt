package de.devbeyer.podcast_sponsorskipper.ui.navigation.playbackController

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.devbeyer.podcast_sponsorskipper.domain.models.db.SponsorSection
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.util.Constants

@Composable
fun FeedbackBar(
    sponsorSection: SponsorSection,
    onEvent: (NavigationEvent) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Is this information correct?\nYour feedback helps us improve.",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.weight(1f),
        )
        IconButton(
            onClick = {
                onEvent(
                    NavigationEvent.RateSponsorSection(
                        sponsorSection = sponsorSection,
                        isPositive = true,
                    )
                )
            },
            modifier = Modifier
                .width(48.dp)
                .padding(Constants.Dimensions.SMALL),
        ) {
            Icon(imageVector = Icons.Filled.ThumbUp, contentDescription = null)
        }
        IconButton(
            onClick = {
                onEvent(
                    NavigationEvent.RateSponsorSection(
                        sponsorSection = sponsorSection,
                        isPositive = false,
                    )
                )
            },
            modifier = Modifier
                .width(48.dp)
                .padding(Constants.Dimensions.SMALL),
        ) {
            Icon(imageVector = Icons.Filled.ThumbDown, contentDescription = null)
        }
    }
}