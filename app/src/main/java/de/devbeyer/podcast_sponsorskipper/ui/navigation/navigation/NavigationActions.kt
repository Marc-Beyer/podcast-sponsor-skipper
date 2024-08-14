package de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.navigation.NavBackStackEntry
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavRoute
import de.devbeyer.podcast_sponsorskipper.util.Constants

@Composable
fun NavigationActions(
    backStackState: NavBackStackEntry?,
    onEvent: (NavigationEvent) -> Unit,
    currentPodcast: PodcastWithRelations?,
    state: NavigationState,
    navigateToSearch: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateUp: () -> Unit,
    navigateToTourGuide: () -> Unit,
) {
    when (backStackState?.destination?.route) {
        NavRoute.Feed.path -> {
            var expanded by remember { mutableStateOf(false) }
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Update",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onEvent(NavigationEvent.UpdatePodcasts)
                    }
                    .padding(Constants.Dimensions.SMALL_MEDIUM),
            )
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        navigateToSearch()
                    }
                    .padding(Constants.Dimensions.SMALL_MEDIUM),
            )
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "More",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        expanded = true
                    }
                    .padding(Constants.Dimensions.SMALL_MEDIUM)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        navigateToSettings()
                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Settings"
                            )
                            Spacer(modifier = Modifier.width(Constants.Dimensions.SMALL))
                            Text(text = "Settings")
                        }
                    },
                )
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        navigateToSearch()
                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                            Spacer(modifier = Modifier.width(Constants.Dimensions.SMALL))
                            Text(text = "Add Podcasts")
                        }
                    },
                )
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onEvent(NavigationEvent.UpdatePodcasts)
                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Update")
                            Spacer(modifier = Modifier.width(Constants.Dimensions.SMALL))
                            Text(text = "Update Podcasts")
                        }
                    },
                )
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        navigateToTourGuide()
                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Outlined.Help, contentDescription = "Help")
                            Spacer(modifier = Modifier.width(Constants.Dimensions.SMALL))
                            Text(text = "Help")
                        }
                    },
                )
            }
        }

        NavRoute.Episodes.path -> {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Update",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        currentPodcast?.let {
                            onEvent(NavigationEvent.UpdatePodcast(it.podcast))
                        }
                    }
                    .padding(Constants.Dimensions.SMALL_MEDIUM),
            )
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Unsubscribe",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onEvent(NavigationEvent.Unsubscribe(currentPodcast))
                    }
                    .padding(Constants.Dimensions.SMALL_MEDIUM),
            )
        }

        NavRoute.Episode.path -> {
            Icon(
                imageVector = if (state.currentNavEpisode?.favorite == true) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Filled.FavoriteBorder
                },
                contentDescription = "Favorite",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        state.currentNavEpisode?.let {
                            onEvent(
                                NavigationEvent.Favorite(
                                    episode = state.currentNavEpisode,
                                    favorite = !state.currentNavEpisode.favorite
                                )
                            )
                        }
                    }
                    .padding(Constants.Dimensions.SMALL_MEDIUM),
            )
        }
    }
}