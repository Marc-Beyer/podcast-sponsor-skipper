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

    @Query("UPDATE episode SET episodePath = :episodePath WHERE episodeUrl = :episodeUrl")
    suspend fun updateEpisodePath(episodeUrl: String, episodePath: String)

    @Query("UPDATE episode SET isCompleted = :isCompleted WHERE episodeUrl = :episodeUrl")
    suspend fun updateEpisodeIsCompleted(episodeUrl: String, isCompleted: Boolean)

    @Query("SELECT * FROM episode WHERE podcastId = :podcastId ORDER BY pubDate DESC")
    fun getEpisodesByPodcastId(podcastId: Int): Flow<List<Episode>>

    @Query("SELECT * FROM episode WHERE imagePath = :imagePath")
    fun getEpisodesByImagePath(imagePath: String): Flow<List<Episode>>

    @Query("SELECT * FROM episode WHERE episodeUrl = :episodeUrl")
    fun getEpisodesByUrl(episodeUrl: String): Flow<Episode?>
}