package de.devbeyer.podcast_sponsorskipper.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface FileAPI {

    @GET
    suspend fun downloadFile(@Url url: String): Response<ResponseBody>
}