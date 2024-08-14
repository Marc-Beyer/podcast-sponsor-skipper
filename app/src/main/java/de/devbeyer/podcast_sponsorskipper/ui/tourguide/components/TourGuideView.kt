package de.devbeyer.podcast_sponsorskipper.ui.tourguide.components

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.devbeyer.podcast_sponsorskipper.ui.Dimensions
import de.devbeyer.podcast_sponsorskipper.ui.theme.PodcastSponsorSkipperTheme
import de.devbeyer.podcast_sponsorskipper.ui.tourguide.TourGuideEvent
import de.devbeyer.podcast_sponsorskipper.ui.tourguide.pages
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TourGuideView(
    onEvent: (TourGuideEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = {
            pages.size
        }
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        HorizontalPager(state = pagerState) { index ->
            TourGuidePage(
                page = pages[index],
                modifier = Modifier.fillMaxHeight(fraction = 0.7f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.large)
                .weight(1f)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TourGuideIndicator(
                currentPage = pagerState.currentPage,
                nrOfPages = pages.size,
                indicatorSize = Dimensions.medium,
                padding = Dimensions.small,
            )

            Button(
                onClick = {

                    if (pagerState.currentPage < pages.size - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                        }
                    } else {
                        onEvent(TourGuideEvent.completedTourGuide)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text =
                    if (pagerState.currentPage < pages.size - 1) {
                        "Next"
                    } else {
                        "Finish"
                    }
                )
            }

            TextButton(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = pagerState.currentPage > 0,
            ) {
                Text(
                    text = if (pagerState.currentPage > 0) {
                        "Back"
                    } else {
                        ""
                    },
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TourGuideViewPreviewDark() {
    PodcastSponsorSkipperTheme {
        Surface {
            TourGuideView(onEvent = {})
        }
    }
}