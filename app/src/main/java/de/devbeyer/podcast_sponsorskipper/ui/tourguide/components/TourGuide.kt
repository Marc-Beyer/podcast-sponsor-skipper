package de.devbeyer.podcast_sponsorskipper.ui.tourguide.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import de.devbeyer.podcast_sponsorskipper.ui.Dimensions
import de.devbeyer.podcast_sponsorskipper.ui.tourguide.pages
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TourGuide() {
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
            .background(MaterialTheme.colorScheme.background),
    ) {
        HorizontalPager(state = pagerState) { index ->
            TourGuidePage(page = pages[index])
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.large)
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