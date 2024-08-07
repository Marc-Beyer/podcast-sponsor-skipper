package de.devbeyer.podcast_sponsorskipper.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationView
import de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation.NavigationViewModel
import de.devbeyer.podcast_sponsorskipper.ui.tourguide.TourGuideViewModel
import de.devbeyer.podcast_sponsorskipper.ui.tourguide.components.TourGuideView

@Composable
fun NavGraph(
    startDestination: String,
    startWithNotificationPermission: () -> Unit,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        navigation(
            route = NavRoute.Start.path,
            startDestination = NavRoute.TourGuide.path
        ){
            composable(
                route = NavRoute.TourGuide.path,
            ){
                val viewModel: TourGuideViewModel = hiltViewModel()
                TourGuideView(
                    onEvent = viewModel::onEvent
                )
            }
        }

        navigation(
            route = NavRoute.Main.path,
            startDestination = NavRoute.Feed.path
        ){
            composable(
                route = NavRoute.Feed.path,
            ){
                startWithNotificationPermission()
                val viewModel: NavigationViewModel = hiltViewModel()
                NavigationView(
                    state = viewModel.state.value,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}