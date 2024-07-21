package de.devbeyer.podcast_sponsorskipper.ui.search

sealed class SearchEvent(){
    data class changeSearch(val search: String): SearchEvent()

    object SearchPodcast: SearchEvent()
}