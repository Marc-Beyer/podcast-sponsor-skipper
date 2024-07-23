package de.devbeyer.podcast_sponsorskipper.data.local.dao

import androidx.room.Dao
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

    @Query("SELECT * FROM podcast")
    fun getPodcasts(): Flow<List<PodcastWithRelations>>

    //@Transaction
    //@Query("SELECT * FROM podcast")
    //fun getAllLive(): LiveData<List<Podcast>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPodcastCategoryCrossRef(crossRef: PodcastCategoryCrossRef)

    @Transaction
    @Query("SELECT * FROM podcast WHERE podcastId = :podcastId")
    fun getPodcastWithRelationsLive(podcastId: Int): Flow<PodcastWithRelations>
}