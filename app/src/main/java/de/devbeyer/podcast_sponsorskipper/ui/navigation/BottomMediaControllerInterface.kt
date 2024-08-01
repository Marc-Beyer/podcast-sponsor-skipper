package de.devbeyer.podcast_sponsorskipper.ui.navigation

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Square
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Category
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.models.db.SponsorSection
import de.devbeyer.podcast_sponsorskipper.ui.common.CoverImage
import de.devbeyer.podcast_sponsorskipper.ui.common.CustomSlider
import de.devbeyer.podcast_sponsorskipper.ui.common.rotationEffect
import de.devbeyer.podcast_sponsorskipper.ui.common.shadowTopOnly
import de.devbeyer.podcast_sponsorskipper.ui.theme.PodcastSponsorSkipperTheme
import de.devbeyer.podcast_sponsorskipper.util.Constants
import de.devbeyer.podcast_sponsorskipper.util.formatMillisecondsToTime
import java.time.LocalDateTime

@Composable
fun BottomMediaControllerInterface(
    state: NavigationState,
    onEvent: (NavigationEvent) -> Unit,
    navigateToEpisode: (Episode, PodcastWithRelations) -> Unit
) {
    val context = LocalContext.current

    if (state.selectedEpisode != null && state.selectedPodcast != null) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .shadowTopOnly(shadowHeight = -50f)
                .padding(16.dp)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            ControllerHeader(
                navigateToEpisode,
                context,
                state.selectedEpisode,
                state.selectedPodcast,
                onEvent,
            )

            Spacer(modifier = Modifier.height(16.dp))

            var sponsorSections = state.sponsorSections
            var sponsorSectionStart: Long? = null
            if (state.sponsorSectionStart != null && state.sponsorSectionEnd != null) {
                sponsorSections = sponsorSections + listOf(
                    SponsorSection(
                        id = 0,
                        endPosition = state.sponsorSectionEnd,
                        startPosition = state.sponsorSectionStart,
                        episodeUrl = ""
                    )
                )
            } else if (state.sponsorSectionStart != null) {
                sponsorSectionStart = state.sponsorSectionStart
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Is this information correct?\nYour feedback helps us improve.",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f),
                )
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.width(48.dp).padding(Constants.Dimensions.SMALL),
                ) {
                    Icon(imageVector = Icons.Filled.ThumbUp, contentDescription = null)
                }
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.width(48.dp).padding(Constants.Dimensions.SMALL),
                ) {
                    Icon(imageVector = Icons.Filled.ThumbDown, contentDescription = null)
                }
            }
            CustomSlider(
                value = if (state.currentPosition < state.duration) state.currentPosition.toFloat() else 0f,
                onValueChange = { onEvent(NavigationEvent.SeekTo(it.toLong())) },
                valueRange = 0f..if (state.duration.toFloat() < 0f) 100f else state.duration.toFloat(),
                sponsorSections = sponsorSections,
                sponsorSectionStart = sponsorSectionStart,
            )
            val curPos = formatMillisecondsToTime(state.currentPosition)
            val duration = formatMillisecondsToTime(state.duration)
            Text(
                text = "$curPos / $duration",
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            ControllerButtons(onEvent, state)

            if (state.sponsorSectionStart != null && state.sponsorSectionEnd != null) {
                ControllerPreview(state, onEvent)
            }
        }
    }
}

@Composable
private fun ControllerPreview(
    state: NavigationState,
    onEvent: (NavigationEvent) -> Unit
) {
    Spacer(modifier = Modifier.height(Constants.Dimensions.MEDIUM))
    Text(
        text = "Sponsor section",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(Constants.Dimensions.SMALL))
    Text(
        text = "Start: ${formatMillisecondsToTime(state.sponsorSectionStart ?: 0)}",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = "End: ${formatMillisecondsToTime(state.sponsorSectionEnd ?: 0)}",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(Constants.Dimensions.MEDIUM))

    when (state.isPreviewing) {
        PreviewState.NONE -> {
            Button(
                onClick = { onEvent(NavigationEvent.Preview) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
            ) {
                Text(
                    text = "Preview the segment",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        PreviewState.PREVIEWING -> {
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Sync,
                    contentDescription = "Episode",
                    modifier = Modifier
                        .padding(1.dp)
                        .rotationEffect(),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        PreviewState.FINISHED -> {
            Button(
                onClick = { onEvent(NavigationEvent.Preview) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
            ) {
                Text(
                    text = "Preview the segment again",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(Constants.Dimensions.SMALL))
            OutlinedButton(
                onClick = { onEvent(NavigationEvent.DiscardSegment) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
            ) {
                Text(
                    text = "Discard Segment",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(Constants.Dimensions.SMALL))
            OutlinedButton(
                onClick = { onEvent(NavigationEvent.SubmitSegment) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
            ) {
                Text(
                    text = "Submit the segment",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(Constants.Dimensions.MEDIUM))
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = Icons.Outlined.Info, contentDescription = null)
        Spacer(modifier = Modifier.width(Constants.Dimensions.MEDIUM))
        Text(
            text = "Please review the highlighted sections before submitting your sponsor segment.",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ControllerButtons(
    onEvent: (NavigationEvent) -> Unit,
    state: NavigationState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        IconButton(onClick = { onEvent(NavigationEvent.SkipBack) }) {
            Icon(imageVector = Icons.Filled.SkipPrevious, contentDescription = null)
        }
        if (state.isPlaying) {
            IconButton(
                onClick = { onEvent(NavigationEvent.Stop) },
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
            ) {
                Icon(
                    imageVector = Icons.Filled.Pause,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        } else {
            IconButton(
                onClick = { onEvent(NavigationEvent.Play) },
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        IconButton(onClick = { onEvent(NavigationEvent.SkipForward) }) {
            Icon(imageVector = Icons.Filled.SkipNext, contentDescription = null)
        }
        if (state.sponsorSectionStart == null || state.sponsorSectionEnd != null) {
            IconButton(
                modifier = Modifier
                    .size(64.dp)
                    .border(
                        BorderStroke(4.dp, MaterialTheme.colorScheme.error),
                        CircleShape
                    ),
                onClick = { onEvent(NavigationEvent.StartSponsorSection) }
            ) {
                Text(
                    text = "AD",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        } else {
            IconButton(
                modifier = Modifier
                    .size(64.dp)
                    .border(
                        BorderStroke(4.dp, MaterialTheme.colorScheme.error),
                        CircleShape
                    ),
                onClick = { onEvent(NavigationEvent.EndSponsorSection) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Square,
                    contentDescription = "End Sponsor Section"
                )
            }
        }
    }
}

@Composable
private fun ControllerHeader(
    navigateToEpisode: (Episode, PodcastWithRelations) -> Unit,
    context: Context,
    selectedEpisode: Episode,
    selectedPodcast: PodcastWithRelations,
    onEvent: (NavigationEvent) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .height(48.dp)
                .aspectRatio(1f)
                .clickable {
                    navigateToEpisode(
                        selectedEpisode,
                        selectedPodcast
                    )
                }
                .background(
                    color = MaterialTheme.colorScheme.primary,
                ),
            contentAlignment = Alignment.Center,
        ) {
            CoverImage(
                context = context,
                imagePath = selectedEpisode.imagePath
                    ?: selectedEpisode.imageUrl
            )
        }
        Spacer(modifier = Modifier.width(Constants.Dimensions.MEDIUM))
        Column(
            modifier = Modifier
                .weight(5f)
                .clickable {
                    navigateToEpisode(
                        selectedEpisode,
                        selectedPodcast
                    )
                },
        ) {
            Text(
                text = selectedEpisode.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = selectedPodcast.podcast.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier.weight(1f),
        ) {
            IconButton(onClick = { onEvent(NavigationEvent.Close) }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BottomMediaControllerInterfacePreview() {
    PodcastSponsorSkipperTheme {
        BottomMediaControllerInterface(
            state = NavigationState(
                selectedPodcast = PodcastWithRelations(
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
                        imagePath = null,
                    ),
                    categories = listOf(Category(1, "Test")),
                ),
                selectedEpisode = Episode(
                    episodeUrl = "",
                    podcastId = 1,
                    episodePath = "",
                    episodeLength = 42,
                    episodeType = "",
                    title = "Episode 01 - Extra Long and overflowing",
                    guid = " 01",
                    link = " 01",
                    pubDate = LocalDateTime.now(),
                    description = " 01",
                    duration = " 01",
                    imageUrl = "https://cdn.changelog.com/uploads/covers/js-party-original.png?v=63725770332",
                    imagePath = null,
                    explicit = false,
                    block = false
                )
            ),
            onEvent = {},
            navigateToEpisode = { _, _ ->

            }
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BottomMediaControllerInterfacePreviewDark() {
    PodcastSponsorSkipperTheme {
        BottomMediaControllerInterface(
            state = NavigationState(
                selectedPodcast = PodcastWithRelations(
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
                        imagePath = null,
                    ),
                    categories = listOf(Category(1, "Test")),
                ),
                selectedEpisode = Episode(
                    episodeUrl = "",
                    podcastId = 1,
                    episodePath = "",
                    episodeLength = 42,
                    episodeType = "",
                    title = "Episode 01 - Extra Long and overflowing",
                    guid = " 01",
                    link = " 01",
                    pubDate = LocalDateTime.now(),
                    description = " 01",
                    duration = " 01",
                    imageUrl = "https://cdn.changelog.com/uploads/covers/js-party-original.png?v=63725770332",
                    imagePath = null,
                    explicit = false,
                    block = false
                )
            ),
            onEvent = {},
            navigateToEpisode = { _, _ ->

            }
        )
    }
}