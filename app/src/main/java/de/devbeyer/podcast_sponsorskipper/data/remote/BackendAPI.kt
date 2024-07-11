package de.devbeyer.podcast_sponsorskipper.data.remote

import de.devbeyer.podcast_sponsorskipper.data.remote.dto.PodcastResponse

interface BackendAPI {
    suspend fun getPodcasts(page:Int): PodcastResponse
}