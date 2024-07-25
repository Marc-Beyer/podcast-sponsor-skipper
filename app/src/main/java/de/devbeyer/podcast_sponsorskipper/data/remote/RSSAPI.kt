package de.devbeyer.podcast_sponsorskipper.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface RSSAPI {

    @GET
    suspend fun getRSSFeed(@Url url: String): Response<String>
}