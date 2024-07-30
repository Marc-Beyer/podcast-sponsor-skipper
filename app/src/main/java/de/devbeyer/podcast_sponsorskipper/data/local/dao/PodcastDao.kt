package de.devbeyer.podcast_sponsorskipper.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastCategoryCrossRef
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import kotlinx.coroutines.flow.Flow

@Dao
interface PodcastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(podcast: Podcast): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPodcastCategoryCrossRef(crossRef: PodcastCategoryCrossRef): Long

    @Delete
    suspend fun deletePodcast(podcast: Podcast)

    @Query("SELECT * FROM podcast")
    fun getPodcasts(): Flow<List<PodcastWithRelations>>

    @Query("SELECT * FROM podcast WHERE url = :url")
    fun getPodcastFromUrl(url: String): Flow<PodcastWithRelations?>

    @Query("SELECT * FROM podcast WHERE podcastId = :id")
    fun getPodcastFromId(id: Int): Flow<PodcastWithRelations?>

    @Transaction
    @Query("SELECT * FROM podcast WHERE podcastId = :podcastId")
    fun getPodcastWithRelations(podcastId: Int): Flow<PodcastWithRelations>
}