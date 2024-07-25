package de.devbeyer.podcast_sponsorskipper.ui.search

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RssFeed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.devbeyer.podcast_sponsorskipper.ui.common.CustomDialog

@Composable
fun AddRSSFeedDialog(
    isAddRSSFeedDialogOpen: Boolean,
    closeAddRSSFeedDialog: () -> Unit,
    onEvent: (SearchEvent) -> Unit,
    state: SearchState
) {
    if (isAddRSSFeedDialogOpen) {
        CustomDialog(
            title = "Add a RSS Podcast",
            onDismissRequest = { closeAddRSSFeedDialog() },
            onAcceptRequest = {
                if (state.rssFeedUrl.isNotBlank()) {
                    onEvent(SearchEvent.AddRSSFeed)
                    closeAddRSSFeedDialog()
                }
            },
            acceptButtonText = "add",
            dismissButtonText = "cancel",
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.RssFeed,
                    contentDescription = "RssFeed",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "RSS Feed Url")
            }
            TextField(
                value = state.rssFeedUrl,
                placeholder = { Text(text = "https://") },
                keyboardActions = KeyboardActions(onDone = {
                    if (state.rssFeedUrl.isNotBlank()) {
                        onEvent(SearchEvent.AddRSSFeed)
                        closeAddRSSFeedDialog()
                    }
                }),
                singleLine = true,
                onValueChange = {
                    onEvent(SearchEvent.ChangeRSSFeedUrl(it))
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}