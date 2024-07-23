package de.devbeyer.podcast_sponsorskipper.domain.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class PodcastWithRelations(
    @Embedded val podcast: Podcast,
    @Relation(
        parentColumn = "podcastId",
        entityColumn = "categoryId",
        associateBy = Junction(PodcastCategoryCrossRef::class)
    )
    val categories: List<Category>,
) : Parcelable