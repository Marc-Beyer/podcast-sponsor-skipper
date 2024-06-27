package de.devbeyer.podcast_sponsorskipper.ui.navigation

sealed class Screen(val route: String) {
    object TourGuide : Screen("tourGuide")
    object Home : Screen("home")
    object Search : Screen("search")
    object Podcast : Screen("podcast")

    object Start : Screen("start")
    object Main : Screen("main")
}
