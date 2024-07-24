package de.devbeyer.podcast_sponsorskipper.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import de.devbeyer.podcast_sponsorskipper.domain.models.Category
import de.devbeyer.podcast_sponsorskipper.domain.models.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.models.PodcastCategoryCrossRef
import de.devbeyer.podcast_sponsorskipper.domain.models.PodcastWithRelations
import kotlinx.coroutines.flow.Flow

@Dao
interface PodcastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(podcast: Podcast)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPodcastCategoryCrossRef(crossRef: PodcastCategoryCrossRef)

    @Delete
    suspend fun deletePodcast(podcast: Podcast)

    @Query("SELECT * FROM podcast")
    fun getPodcasts(): Flow<List<PodcastWithRelations>>

    //@Transaction
    //@Query("SELECT * FROM podcast")
    //fun getAllLive(): LiveData<List<Podcast>>
    @Query("SELECT * FROM podcast WHERE url = :url")
    fun getPodcastFromUrl(url: String): Flow<Podcast?>

    @Transaction
    @Query("SELECT * FROM podcast WHERE podcastId = :podcastId")
    fun getPodcastWithRelations(podcastId: Int): Flow<PodcastWithRelations>
}