package de.devbeyer.podcast_sponsorskipper.ui.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
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
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.episode.EpisodeView
import de.devbeyer.podcast_sponsorskipper.ui.episode.EpisodeViewModel
import de.devbeyer.podcast_sponsorskipper.ui.episodes.EpisodesView
import de.devbeyer.podcast_sponsorskipper.ui.episodes.EpisodesViewModel
import de.devbeyer.podcast_sponsorskipper.ui.feed.FeedView
import de.devbeyer.podcast_sponsorskipper.ui.feed.FeedViewModel
import de.devbeyer.podcast_sponsorskipper.ui.info.InfoView
import de.devbeyer.podcast_sponsorskipper.ui.info.InfoViewModel
import de.devbeyer.podcast_sponsorskipper.ui.navigation.playbackController.PlaybackController
import de.devbeyer.podcast_sponsorskipper.ui.search.SearchView
import de.devbeyer.podcast_sponsorskipper.ui.search.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
                backStackState?.destination?.route == NavRoute.Episodes.path ||
                backStackState?.destination?.route == NavRoute.Episode.path
    }

    var isAddRSSFeedDialogOpen by remember { mutableStateOf(false) }

    val currentPodcast = navController
        .previousBackStackEntry
        ?.savedStateHandle
        ?.get<PodcastWithRelations?>(
            "podcastWithRelations"
        )

    val currentEpisode = navController
        .previousBackStackEntry
        ?.savedStateHandle
        ?.get<Episode?>(
            "episode"
        )


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (backStackState?.destination?.route) {
                            NavRoute.Feed.path -> "Podcasts"
                            NavRoute.Search.path -> "Add podcast"
                            NavRoute.Info.path -> currentPodcast?.podcast?.title ?: "Podcast"
                            NavRoute.Episodes.path -> currentPodcast?.podcast?.title ?: "Podcast"
                            NavRoute.Episode.path -> currentEpisode?.title ?: "Episode"
                            else -> ""
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.basicMarquee(),
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
                                        currentPodcast?.let {
                                            onEvent(NavigationEvent.UpdatePodcast(it.podcast))
                                        }
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
            PlaybackController(
                state = state,
                onEvent = onEvent,
                navigateToEpisode = { episode, podcastWithRelations ->
                    navigateToEpisode(
                        navController = navController,
                        episode = episode,
                        podcastWithRelations = podcastWithRelations,
                    )
                }
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
                    },
                    navigateToSearch = {
                        navigateToSearch(
                            navController = navController,
                        )
                    },
                )

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
                            navigationState = state,
                            onEvent = viewModel::onEvent,
                            onNavigationEvent = onEvent,
                            navigateToEpisode = { episode, podcastWithRelations ->
                                navigateToEpisode(
                                    navController = navController,
                                    episode = episode,
                                    podcastWithRelations = podcastWithRelations,
                                )
                            },
                        )
                    }

            }
            composable(route = NavRoute.Episode.path) {
                val viewModel: EpisodeViewModel = hiltViewModel()
                val podcastWithRelations =
                    navController.previousBackStackEntry?.savedStateHandle?.get<PodcastWithRelations?>(
                        "podcastWithRelations"
                    )
                val episode = navController.previousBackStackEntry?.savedStateHandle?.get<Episode?>(
                    "episode"
                )
                if (podcastWithRelations != null && episode != null) {
                    viewModel.setEpisode(episode, podcastWithRelations)
                    EpisodeView(
                        state = viewModel.state.value,
                        navigationState = state,
                        onEvent = viewModel::onEvent,
                        onNavigationEvent = onEvent,
                    )
                }

            }
        }
    }
}

private fun navigateToEpisode(
    navController: NavController,
    episode: Episode,
    podcastWithRelations: PodcastWithRelations
) {
    navController.currentBackStackEntry?.savedStateHandle?.set(
        "podcastWithRelations",
        podcastWithRelations
    )
    navController.currentBackStackEntry?.savedStateHandle?.set(
        "episode",
        episode
    )
    navController.navigate(route = NavRoute.Episode.path)
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