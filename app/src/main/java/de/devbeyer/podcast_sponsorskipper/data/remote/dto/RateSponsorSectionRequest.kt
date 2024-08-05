package de.devbeyer.podcast_sponsorskipper.data.remote.dto

data class RateSponsorSectionRequest(
    val sponsorSectionId: Long,
    val isPositive: Boolean,
    val duration: Long,
    val username: String,
    val token: String,
)
