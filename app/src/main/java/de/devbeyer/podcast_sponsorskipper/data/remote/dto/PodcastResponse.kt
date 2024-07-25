package de.devbeyer.podcast_sponsorskipper.data.remote.dto

import de.devbeyer.podcast_sponsorskipper.domain.models.db.Category

data class PodcastResponse(
    val podcasts: List<PodcastsResponse>,
    val nrOfPages: Int,
)


data class PodcastsResponse(
    val id: Int,
    val url: String,
    val title: String,
    val description: String,
    val link: String,
    val language: String,
    val imageUrl: String,
    val explicit: Boolean,
    val locked: Boolean,
    val complete: Boolean,
    val lastUpdate: String,
    val nrOdEpisodes: Int,
    val copyright: String?,
    val author: String?,
    val fundingText: String?,
    val fundingUrl: String?,
    val categories: List<Category>
)