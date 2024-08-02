package de.devbeyer.podcast_sponsorskipper.ui.navigation.playbackController

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Category
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.models.db.SponsorSection
import de.devbeyer.podcast_sponsorskipper.ui.common.CustomSlider
import de.devbeyer.podcast_sponsorskipper.ui.common.shadowTopOnly
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavigationEvent
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavigationState
import de.devbeyer.podcast_sponsorskipper.ui.theme.PodcastSponsorSkipperTheme
import de.devbeyer.podcast_sponsorskipper.util.formatMillisecondsToTime
import java.time.LocalDateTime

@Composable
fun PlaybackController(
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
                        episodeUrl = "",
                        isRated = true,
                        isProvisional = false,
                    )
                )
            } else if (state.sponsorSectionStart != null) {
                sponsorSectionStart = state.sponsorSectionStart
            }

            val sponsorSectionAtPosition = getSponsorSectionAtPosition(
                sponsorSections = sponsorSections,
                currentPosition = state.currentPosition
            )
            if(sponsorSectionAtPosition != null){
                FeedbackBar(
                    sponsorSection = sponsorSectionAtPosition,
                    onEvent = onEvent,
                )
            }
            CustomSlider(
                value = if (state.currentPosition < state.duration) state.currentPosition.toFloat() else 0f,
                onValueChange = { onEvent(NavigationEvent.SeekTo(it.toLong())) },
                valueRange = 0f..if (state.duration.toFloat() < 0f) 100f else state.duration.toFloat(),
                sponsorSections = sponsorSections,
                isInsideOfSponsorSection = sponsorSectionAtPosition != null,
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

private fun getSponsorSectionAtPosition(
    currentPosition: Long,
    sponsorSections: List<SponsorSection>,
): SponsorSection? {
    for (section in sponsorSections) {
        if (currentPosition in section.startPosition..section.endPosition) {
            return section
        }
    }
    return null
}


@Preview(showBackground = true)
@Composable
fun PlaybackControllerPreview() {
    PodcastSponsorSkipperTheme {
        PlaybackController(
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
fun PlaybackControllerPreviewDark() {
    PodcastSponsorSkipperTheme {
        PlaybackController(
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