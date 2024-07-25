package de.devbeyer.podcast_sponsorskipper.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    state: SearchState,
    onEvent: (SearchEvent) -> Unit,
    navigateToInfo: (PodcastWithRelations) -> Unit
) {
    var isSearchBarActive by remember { mutableStateOf(false) }

    Column() {
        SearchBar(
            query = state.search,
            onQueryChange = { onEvent(SearchEvent.changeSearch(it)) },
            onSearch = {
                isSearchBarActive = false
                onEvent(SearchEvent.SearchPodcast)
            },
            active = isSearchBarActive,
            onActiveChange = { isSearchBarActive = it },
            placeholder = {
                Text(text = "Search Podcast")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            },
            windowInsets = WindowInsets(top = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
        ) {

        }
        state.podcastsWithRelations?.let {
            PodcastList(
                modifier = Modifier.padding(0.dp),
                podcastsWithRelations = state.podcastsWithRelations.collectAsLazyPagingItems(),
                onClick = {
                    navigateToInfo(it)
                }
            )
        }
    }
}