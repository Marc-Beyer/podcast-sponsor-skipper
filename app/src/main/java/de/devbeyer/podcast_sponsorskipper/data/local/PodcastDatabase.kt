package de.devbeyer.podcast_sponsorskipper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.devbeyer.podcast_sponsorskipper.data.local.dao.CategoryDao
import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.data.local.dao.PodcastDao
import de.devbeyer.podcast_sponsorskipper.domain.models.Category
import de.devbeyer.podcast_sponsorskipper.domain.models.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.models.PodcastCategoryCrossRef

@Database(
    entities = [Podcast::class, Category::class, PodcastCategoryCrossRef::class, Episode::class],
    version = 1
)
@TypeConverters(CustomTypeConverter::class)
abstract class PodcastDatabase : RoomDatabase() {
    abstract fun podcastDao(): PodcastDao
    abstract fun categoryDao(): CategoryDao
    abstract fun episodeDao(): EpisodeDao
}