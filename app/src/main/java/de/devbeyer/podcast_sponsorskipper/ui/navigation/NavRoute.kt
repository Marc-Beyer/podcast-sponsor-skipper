package de.devbeyer.podcast_sponsorskipper.ui.navigation

sealed class NavRoute(val path: String) {
    object TourGuide : NavRoute("tourGuide")
    object Feed : NavRoute("feed")
    object Episodes : NavRoute("episodes")
    object Episode : NavRoute("episode")
    object Search : NavRoute("search")
    object Info : NavRoute("info")
    object Settings : NavRoute("Settings")

    object Start : NavRoute("start")
    object Main : NavRoute("main")
}
