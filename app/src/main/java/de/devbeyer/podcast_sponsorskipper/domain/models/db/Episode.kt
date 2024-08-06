package de.devbeyer.podcast_sponsorskipper.domain.models.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity(
    tableName = "episode",
    foreignKeys = [
        ForeignKey(
            entity = Podcast::class,
            parentColumns = ["podcastId"],
            childColumns = ["podcastId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["podcastId"])]
)
data class Episode(
    @PrimaryKey
    val episodeUrl: String,

    val podcastId: Int,

    var episodePath: String?,
    var episodeLength: Int,
    var episodeType: String,
    val title: String,
    val guid: String,
    val link: String,
    val pubDate: LocalDateTime,
    val description: String,
    val duration: String,
    var imageUrl: String,
    var imagePath: String?,
    var explicit: Boolean,
    var block: Boolean,
    var isCompleted: Boolean = false,
    var favorite: Boolean = false,
) : Parcelable
