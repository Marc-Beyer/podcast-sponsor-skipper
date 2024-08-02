package de.devbeyer.podcast_sponsorskipper.data.remote.dto

data class SubmitSponsorSectionBody(
    val episodeUrl: String,
    val podcastUrl: String,
    val startPosition: Long,
    val endPosition: Long,
    val duration: Long,
    val username: String,
    val token: String,
)
