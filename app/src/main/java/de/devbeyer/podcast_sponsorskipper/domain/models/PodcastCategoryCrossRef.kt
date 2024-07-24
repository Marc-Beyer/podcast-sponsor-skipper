package de.devbeyer.podcast_sponsorskipper.domain.models

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["podcastId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = Podcast::class,
            parentColumns = ["podcastId"],
            childColumns = ["podcastId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class PodcastCategoryCrossRef(
    val podcastId: Int,
    val categoryId: Int
)
