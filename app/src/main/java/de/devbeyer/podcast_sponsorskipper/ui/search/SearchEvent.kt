package de.devbeyer.podcast_sponsorskipper.ui.search

sealed class SearchEvent {
    data class ChangeSearch(val search: String): SearchEvent()
    data class ChangeRSSFeedUrl(val rssFeedUrl: String): SearchEvent()

    object SearchPodcast: SearchEvent()
    object AddRSSFeed: SearchEvent()
    object SearchOnline: SearchEvent()
}