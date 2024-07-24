package de.devbeyer.podcast_sponsorskipper.domain.models

import androidx.room.Entity

@Entity(primaryKeys = ["podcastId", "categoryId"])
data class PodcastCategoryCrossRef(
    val podcastId: Int,
    val categoryId: Int
)
