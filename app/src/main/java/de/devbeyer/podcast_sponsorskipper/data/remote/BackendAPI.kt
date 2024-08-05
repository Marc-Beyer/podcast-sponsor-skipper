package de.devbeyer.podcast_sponsorskipper.data.remote

import de.devbeyer.podcast_sponsorskipper.data.remote.dto.PodcastResponse
import de.devbeyer.podcast_sponsorskipper.data.remote.dto.RateSponsorSectionRequest
import de.devbeyer.podcast_sponsorskipper.data.remote.dto.SponsorSectionRequest
import de.devbeyer.podcast_sponsorskipper.data.remote.dto.SubmitSponsorSectionBody
import de.devbeyer.podcast_sponsorskipper.domain.models.UserData
import de.devbeyer.podcast_sponsorskipper.domain.models.db.SponsorSection
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @POST("submit-sponsor-section")
    suspend fun submitSponsorSection (
        @Body submitSponsorSectionBody: SubmitSponsorSectionBody
    ): Response<Long>

    @GET("register")
    suspend fun register (): Response<UserData>

    @POST("get-sponsor-section")
    suspend fun getSponsorSection(
        @Body request: SponsorSectionRequest
    ): Response<List<SponsorSection>>


    @POST("rate-sponsor-section")
    suspend fun rateSponsorSection(
        @Body request: RateSponsorSectionRequest
    ): Response<Float>
}