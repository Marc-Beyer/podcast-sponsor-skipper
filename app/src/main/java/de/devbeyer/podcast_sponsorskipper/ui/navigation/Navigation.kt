package de.devbeyer.podcast_sponsorskipper.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.devbeyer.podcast_sponsorskipper.domain.models.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.ui.info.InfoViewModel
import de.devbeyer.podcast_sponsorskipper.ui.info.InfoView
import de.devbeyer.podcast_sponsorskipper.ui.search.SearchView
import de.devbeyer.podcast_sponsorskipper.ui.search.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Podcasts",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Update",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {

                            }
                    )
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoute.Home.path,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = NavRoute.Home.path) { backStackEntry ->
                val viewModel: SearchViewModel = hiltViewModel()
                SearchView(
                    state = viewModel.state.value,
                    onEvent = viewModel::onEvent,
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
                navController.previousBackStackEntry?.savedStateHandle?.get<PodcastWithRelations?>("podcastWithRelations")
                    ?.let { podcastWithRelations ->
                        viewModel.setPodcast(podcastWithRelations)
                        InfoView(
                            state = viewModel.state.value,
                            onEvent = viewModel::onEvent,
                            //navigateUp = { navController.navigateUp() }
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