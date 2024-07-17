package de.devbeyer.podcast_sponsorskipper.data.remote

import de.devbeyer.podcast_sponsorskipper.data.remote.dto.PodcastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BackendAPI {
    @GET("podcasts")
    suspend fun getPodcasts(
        @Query("page")page:Int,
    ): PodcastResponse

    @GET("podcasts")
    suspend fun searchPodcasts(
        @Query("page")page:Int,
        @Query("search")search:String,
    ): PodcastResponse
}