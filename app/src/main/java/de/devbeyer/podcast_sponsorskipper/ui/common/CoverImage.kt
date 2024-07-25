package de.devbeyer.podcast_sponsorskipper.ui.common

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import de.devbeyer.podcast_sponsorskipper.R

@Composable
fun CoverImage(
    context: Context,
    imagePath: String?,
) {
    AsyncImage(
        model = ImageRequest.Builder(context).data(imagePath).build(),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
        error = painterResource(id = R.drawable.ic_launcher_foreground),
        fallback = painterResource(id = R.drawable.ic_launcher_foreground),
    )
}