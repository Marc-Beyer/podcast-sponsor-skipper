package de.devbeyer.podcast_sponsorskipper.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(episode: Episode)

    @Update
    suspend fun update(episode: Episode)

    @Query("SELECT * FROM episode WHERE podcastId = :podcastId ORDER BY pubDate DESC")
    fun getEpisodesByPodcastUrl(podcastId: Int): Flow<List<Episode>>
}