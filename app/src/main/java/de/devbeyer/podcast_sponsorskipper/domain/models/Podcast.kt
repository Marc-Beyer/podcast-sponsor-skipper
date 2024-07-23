package de.devbeyer.podcast_sponsorskipper.domain.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(
    tableName = "podcast",
)
data class Podcast(
    @PrimaryKey
    @ColumnInfo(name = "podcastId")
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
    //val categories: List<Category>
) : Parcelable

//data class PodcastAndEpisodes(
//    val podcastWithRelations: PodcastWithRelations,
//    val episodes: List<Episode>,
//)