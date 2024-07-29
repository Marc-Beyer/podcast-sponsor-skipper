package de.devbeyer.podcast_sponsorskipper.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.devbeyer.podcast_sponsorskipper.domain.models.db.SponsorSection


@Composable
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    sponsorSections: List<SponsorSection>,
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val sponsorColor = MaterialTheme.colorScheme.error


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        // Canvas for custom track background
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .align(Alignment.Center)
        ) {
            val totalWidth = size.width
            val height = size.height

            drawRect(
                color = primaryColor,
                topLeft = Offset(x = 0f, y = 0f),
                size = Size(width = totalWidth, height = height)
            )

            sponsorSections.forEach {sponsorSection ->
                val sectionStart = ((sponsorSection.startPosition - valueRange.start) / (valueRange.endInclusive - valueRange.start)) * totalWidth
                val sectionEnd = ((sponsorSection.endPosition - valueRange.start) / (valueRange.endInclusive - valueRange.start)) * totalWidth
                val sectionWidth = sectionEnd - sectionStart

                drawRect(
                    color = sponsorColor,
                    topLeft = Offset(x = sectionStart, y = 0f),
                    size = Size(width = sectionWidth, height = height)
                )
            }
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            colors = SliderDefaults.colors(
                thumbColor = determineThumbColor(value.toLong(), sponsorSections),
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            )
        )
    }
}

@Composable
fun determineThumbColor(sliderPosition: Long, sponsorSections: List<SponsorSection>): Color {
    for (section in sponsorSections) {
        if (sliderPosition in section.startPosition..section.endPosition) {
            return MaterialTheme.colorScheme.error
        }
    }
    return MaterialTheme.colorScheme.primary
}