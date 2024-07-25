package de.devbeyer.podcast_sponsorskipper.ui.navigation

sealed class NavRoute(val path: String) {
    object TourGuide : NavRoute("tourGuide")
    object Feed : NavRoute("feed")
    object Episodes : NavRoute("home")
    object Search : NavRoute("search")
    object Info : NavRoute("info")

    object Start : NavRoute("start")
    object Main : NavRoute("main")
}
