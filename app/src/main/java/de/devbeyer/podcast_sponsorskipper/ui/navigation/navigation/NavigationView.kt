package de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import de.devbeyer.podcast_sponsorskipper.ui.common.dialog.ConfirmUnsubscribeDialog
import de.devbeyer.podcast_sponsorskipper.ui.common.useMarquee
import de.devbeyer.podcast_sponsorskipper.ui.episode.EpisodeView
import de.devbeyer.podcast_sponsorskipper.ui.episode.EpisodeViewModel
import de.devbeyer.podcast_sponsorskipper.ui.episodes.EpisodesView
import de.devbeyer.podcast_sponsorskipper.ui.episodes.EpisodesViewModel
import de.devbeyer.podcast_sponsorskipper.ui.feed.FeedView
import de.devbeyer.podcast_sponsorskipper.ui.feed.FeedViewModel
import de.devbeyer.podcast_sponsorskipper.ui.info.InfoView
import de.devbeyer.podcast_sponsorskipper.ui.info.InfoViewModel
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavRoute
import de.devbeyer.podcast_sponsorskipper.ui.navigation.playbackController.PlaybackController
import de.devbeyer.podcast_sponsorskipper.ui.search.SearchView
import de.devbeyer.podcast_sponsorskipper.ui.search.SearchViewModel
import de.devbeyer.podcast_sponsorskipper.ui.settings.SettingsView
import de.devbeyer.podcast_sponsorskipper.ui.settings.SettingsViewModel
import de.devbeyer.podcast_sponsorskipper.util.Constants

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NavigationView(
    state: NavigationState,
    onEvent: (NavigationEvent) -> Unit,
    navigateToTourGuide: () -> Unit,
) {
    val navController = rememberNavController()
    onEvent(NavigationEvent.SetNavigateUp(navigateUp = { navController.navigateUp() }))
    val backStackState = navController.currentBackStackEntryAsState().value

    val isBackArrowVisible = remember(key1 = backStackState) {
        backStackState?.destination?.route != NavRoute.Feed.path
    }

    var isAddRSSFeedDialogOpen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (backStackState?.destination?.route) {
                            NavRoute.Feed.path -> "Podcasts"
                            NavRoute.Search.path -> "Add podcast"
                            NavRoute.Info.path -> state.currentNavPodcast?.podcast?.title
                                ?: "Podcast"

                            NavRoute.Episodes.path -> state.currentNavPodcast?.podcast?.title
                                ?: "Podcast"

                            NavRoute.Episode.path -> state.currentNavEpisode?.title ?: "Episode"
                            NavRoute.Settings.path -> "Settings"
                            else -> ""
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.useMarquee(state.settings.enableMarquee),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    NavigationActions(
                        backStackState = backStackState,
                        onEvent = onEvent,
                        currentPodcast = state.currentNavPodcast,
                        state = state,
                        navigateToSearch = { navigateToSearch(navController) },
                        navigateToSettings = { navController.navigate(route = NavRoute.Settings.path) },
                        navigateUp = { navController.navigateUp() },
                        navigateToTourGuide = navigateToTourGuide,
                    )
                },
                navigationIcon = {
                    if (isBackArrowVisible) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    navController.navigateUp()
                                }
                                .padding(Constants.Dimensions.MEDIUM),
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
                            modifier = Modifier.padding(Constants.Dimensions.MEDIUM),
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
                        onEvent = onEvent,
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
                ConfirmUnsubscribeDialog(
                    state = state,
                    onEvent = onEvent,
                )

                val viewModel: FeedViewModel = hiltViewModel()
                FeedView(
                    state = viewModel.state.value,
                    onEvent = viewModel::onEvent,
                    navigationState = state,
                    onNavigationEvent = onEvent,
                    navigateToEpisodes = { podcastWithRelations ->
                        navigateToEpisodes(
                            navController = navController,
                            podcastWithRelations = podcastWithRelations,
                            onEvent = onEvent,
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
                    navigationState = state,
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
                        LaunchedEffect(podcastWithRelations) {
                            viewModel.setPodcast(podcastWithRelations)
                        }
                        InfoView(
                            state = viewModel.state.value,
                            onEvent = viewModel::onEvent,
                            gotoEpisodes = { podcastWithRelations ->
                                navigateToEpisodes(
                                    navController = navController,
                                    podcastWithRelations = podcastWithRelations,
                                    onEvent = onEvent,
                                    popAll = true,
                                )
                            },
                        )
                    }

            }
            composable(route = NavRoute.Settings.path) {
                val viewModel: SettingsViewModel = hiltViewModel()
                SettingsView(
                    state = viewModel.state.value,
                    onEvent = viewModel::onEvent,
                    navigationState = state,
                    onNavigationEvent = onEvent,
                )
            }
            composable(route = NavRoute.Episodes.path) {
                state.currentNavPodcast?.let { podcastWithRelations ->
                    ConfirmUnsubscribeDialog(
                        state = state,
                        onEvent = onEvent,
                    )

                    val viewModel: EpisodesViewModel = hiltViewModel()
                    LaunchedEffect(podcastWithRelations) {
                        viewModel.setPodcast(podcastWithRelations = podcastWithRelations)
                    }
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
                                onEvent = onEvent,
                            )
                        },
                    )
                } ?: run { navController.navigateUp() }
            }
            composable(route = NavRoute.Episode.path) {
                val viewModel: EpisodeViewModel = hiltViewModel()
                state.currentNavPodcast?.let { podcastWithRelations ->
                    state.currentNavEpisode?.let { episode ->
                        LaunchedEffect(episode, podcastWithRelations) {
                            viewModel.setEpisode(episode, podcastWithRelations)
                        }
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
}

private fun navigateToEpisode(
    navController: NavController,
    episode: Episode,
    podcastWithRelations: PodcastWithRelations,
    onEvent: (NavigationEvent) -> Unit,
) {
    onEvent(NavigationEvent.ChangeCurNavEpisode(episode))
    if (navController.currentBackStackEntry?.destination?.route != NavRoute.Episode.path) {
        navController.navigate(route = NavRoute.Episode.path)
    }
}

private fun navigateToInfo(
    navController: NavController,
    podcastWithRelations: PodcastWithRelations,
) {
    navController.currentBackStackEntry?.savedStateHandle?.set(
        "podcastWithRelations",
        podcastWithRelations
    )
    navController.navigate(route = NavRoute.Info.path)
}

private fun navigateToEpisodes(
    navController: NavController,
    podcastWithRelations: PodcastWithRelations,
    onEvent: (NavigationEvent) -> Unit,
    popAll: Boolean = false,
) {
    Log.i("AAA", NavRoute.Episodes.path)
    onEvent(NavigationEvent.ChangeCurNavPodcast(podcastWithRelations = podcastWithRelations))
    navController.navigate(route = NavRoute.Episodes.path) {
        if (popAll) {
            popUpTo(route = NavRoute.Feed.path) {
                inclusive = false
            }
        }
    }
}

private fun navigateToSearch(
    navController: NavController,
) {
    navController.navigate(route = NavRoute.Search.path)
}