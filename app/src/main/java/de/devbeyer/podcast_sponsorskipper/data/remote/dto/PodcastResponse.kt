package de.devbeyer.podcast_sponsorskipper.data.remote.dto

import de.devbeyer.podcast_sponsorskipper.domain.models.Podcast

data class PodcastResponse(
    val podcasts: List<Podcast>,
    val nrOfPages: Int,
)