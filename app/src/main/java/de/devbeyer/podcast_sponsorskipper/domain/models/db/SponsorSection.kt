package de.devbeyer.podcast_sponsorskipper.domain.models.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "sponsorSection",
    foreignKeys = [
        ForeignKey(
            entity = Episode::class,
            parentColumns = ["episodeUrl"],
            childColumns = ["episodeUrl"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["episodeUrl"])]
)
data class SponsorSection(
    @PrimaryKey
    val id: Long,
    val episodeUrl: String,
    val startPosition: Long,
    val endPosition: Long,
) : Parcelable
