package de.devbeyer.podcast_sponsorskipper.ui.navigation

import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
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
                Text(text = "Home")
            }
        }
    }
}