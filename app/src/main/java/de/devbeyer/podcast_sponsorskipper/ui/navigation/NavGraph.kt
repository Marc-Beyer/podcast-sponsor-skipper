package de.devbeyer.podcast_sponsorskipper.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import de.devbeyer.podcast_sponsorskipper.ui.search.SearchView
import de.devbeyer.podcast_sponsorskipper.ui.search.SearchViewModel
import de.devbeyer.podcast_sponsorskipper.ui.tourguide.TourGuideViewModel
import de.devbeyer.podcast_sponsorskipper.ui.tourguide.components.TourGuide

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination){
        navigation(
            route = Screen.Start.route,
            startDestination = Screen.TourGuide.route
        ){
            composable(
                route = Screen.TourGuide.route,
            ){
                val viewModel: TourGuideViewModel = hiltViewModel()
                TourGuide(
                    onEvent = viewModel::onEvent
                )
            }
        }

        navigation(
            route = Screen.Main.route,
            startDestination = Screen.Home.route
        ){
            composable(
                route = Screen.Home.route,
            ){
                val viewModel: SearchViewModel = hiltViewModel()
                SearchView(
                    state = viewModel.state.value,
                    onEvent = viewModel::onEvent,
                    navigate = {}
                )
            }
        }
    }
}