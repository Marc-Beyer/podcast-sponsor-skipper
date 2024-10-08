package de.devbeyer.podcast_sponsorskipper.ui.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.loadingEffect() = composed {
    val transition = rememberInfiniteTransition(label = "")
    val alpha = transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    ).value
    this.background(
        color = MaterialTheme.colorScheme.primary.copy(alpha),
    )
}

fun Modifier.rotationEffect() = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "DownloadingAnimation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "Rotation"
    )

    this.graphicsLayer {
        rotationZ = rotation
    }
}

fun Modifier.shadowTopOnly(
    shadowColor: Color = Color.Black,
    alpha: Float = 0.5f,
    shadowHeight: Float = 10f
) = this.then(
    Modifier.drawBehind {
        val transparent = shadowColor.copy(alpha = 0f)
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(shadowColor.copy(alpha = alpha), transparent),
                startY = 0f,
                endY = shadowHeight
            ),
            size = size.copy(height = shadowHeight)
        )
    }
)

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.useMarquee(enabled: Boolean): Modifier {
    return if (enabled) {
        this.basicMarquee()
    } else {
        this
    }
}