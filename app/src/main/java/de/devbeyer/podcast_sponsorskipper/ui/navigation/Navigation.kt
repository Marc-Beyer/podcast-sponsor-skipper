package de.devbeyer.podcast_sponsorskipper.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.episodes.EpisodesView
import de.devbeyer.podcast_sponsorskipper.ui.episodes.EpisodesViewModel
import de.devbeyer.podcast_sponsorskipper.ui.feed.FeedView
import de.devbeyer.podcast_sponsorskipper.ui.feed.FeedViewModel
import de.devbeyer.podcast_sponsorskipper.ui.info.InfoView
import de.devbeyer.podcast_sponsorskipper.ui.info.InfoViewModel
import de.devbeyer.podcast_sponsorskipper.ui.search.SearchView
import de.devbeyer.podcast_sponsorskipper.ui.search.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    state: NavigationState,
    onEvent: (NavigationEvent) -> Unit,
) {
    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableStateOf(0)
    }
    selectedItem = when (backStackState?.destination?.route) {
        NavRoute.Feed.path -> 0
        NavRoute.Search.path -> 1
        NavRoute.Info.path -> 2
        else -> 0
    }

    val isBackArrowVisible = remember(key1 = backStackState) {
        backStackState?.destination?.route == NavRoute.Search.path ||
                backStackState?.destination?.route == NavRoute.Info.path ||
                backStackState?.destination?.route == NavRoute.Episodes.path
    }

    var isAddRSSFeedDialogOpen by remember { mutableStateOf(false) }

    var currentPodcast = navController
        .previousBackStackEntry
        ?.savedStateHandle
        ?.get<PodcastWithRelations?>(
            "podcastWithRelations"
        )


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (backStackState?.destination?.route) {
                            NavRoute.Feed.path -> "Podcasts"
                            NavRoute.Search.path -> "Add podcast"
                            NavRoute.Info.path -> "Podcast"
                            NavRoute.Episodes.path -> currentPodcast?.podcast?.title ?: "Podcast"

                            else -> ""
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    when (backStackState?.destination?.route) {
                        NavRoute.Feed.path -> {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add Podcast",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable {
                                        navigateToSearch(navController)
                                    }
                            )
                        }

                        NavRoute.Episodes.path -> {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Update",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable {

                                    }
                            )
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Unsubscribe",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable {
                                        onEvent(NavigationEvent.Unsubscribe(currentPodcast))
                                        navController.navigateUp()
                                    }
                            )
                        }
                    }
                },
                navigationIcon = {
                    if (isBackArrowVisible) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    navController.navigateUp()
                                }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            when (backStackState?.destination?.route) {
                NavRoute.Search.path -> {
                    FloatingActionButton(onClick = { isAddRSSFeedDialogOpen = true }) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(Icons.Default.RssFeed, contentDescription = "Add RSS Feed")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Add RSS Feed")
                        }
                    }
                }
            }
        },
        bottomBar = {
            BottomMediaControllerInterface(
                state = state,
                onEvent = onEvent
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoute.Feed.path,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = NavRoute.Feed.path) {
                val viewModel: FeedViewModel = hiltViewModel()
                FeedView(
                    state = viewModel.state.value,
                    navigateToEpisodes = { podcastWithRelations ->
                        navigateToEpisodes(
                            navController = navController,
                            podcastWithRelations = podcastWithRelations
                        )
                    })
            }
            composable(route = NavRoute.Search.path) { backStackEntry ->
                val viewModel: SearchViewModel = hiltViewModel()
                SearchView(
                    state = viewModel.state.value,
                    onEvent = viewModel::onEvent,
                    isAddRSSFeedDialogOpen = isAddRSSFeedDialogOpen,
                    closeAddRSSFeedDialog = { isAddRSSFeedDialogOpen = false },
                    navigateToInfo = { podcastWithRelations ->
                        navigateToInfo(
                            navController = navController,
                            podcastWithRelations = podcastWithRelations
                        )
                    },
                )
            }
            composable(route = NavRoute.Info.path) {
                val viewModel: InfoViewModel = hiltViewModel()
                navController.previousBackStackEntry?.savedStateHandle?.get<PodcastWithRelations?>(
                    "podcastWithRelations"
                )
                    ?.let { podcastWithRelations ->
                        viewModel.setPodcast(podcastWithRelations)
                        InfoView(
                            state = viewModel.state.value,
                            onEvent = viewModel::onEvent,
                            //navigateUp = { navController.navigateUp() }
                        )
                    }

            }
            composable(route = NavRoute.Episodes.path) {
                val viewModel: EpisodesViewModel = hiltViewModel()
                navController.previousBackStackEntry?.savedStateHandle?.get<PodcastWithRelations?>(
                    "podcastWithRelations"
                )
                    ?.let { podcastWithRelations ->
                        viewModel.setPodcast(podcastWithRelations)
                        EpisodesView(
                            state = viewModel.state.value,
                            onEvent = viewModel::onEvent,
                            onNavigationEvent = onEvent,
                        )
                    }

            }
        }
    }
}

private fun navigateToInfo(
    navController: NavController,
    podcastWithRelations: PodcastWithRelations
) {
    navController.currentBackStackEntry?.savedStateHandle?.set(
        "podcastWithRelations",
        podcastWithRelations
    )
    navController.navigate(route = NavRoute.Info.path)
}

private fun navigateToEpisodes(
    navController: NavController,
    podcastWithRelations: PodcastWithRelations
) {
    navController.currentBackStackEntry?.savedStateHandle?.set(
        "podcastWithRelations",
        podcastWithRelations
    )
    navController.navigate(route = NavRoute.Episodes.path)
}

private fun navigateToSearch(
    navController: NavController,
) {
    navController.navigate(route = NavRoute.Search.path)
}