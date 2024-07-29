package de.devbeyer.podcast_sponsorskipper.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.devbeyer.podcast_sponsorskipper.domain.models.db.SponsorSection
import kotlinx.coroutines.flow.Flow

@Dao
interface SponsorSectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sponsorSection: SponsorSection)

    @Update
    suspend fun update(sponsorSection: SponsorSection)

    @Delete
    suspend fun delete(sponsorSection: SponsorSection)

    @Query("SELECT * FROM sponsorSection")
    fun getSponsorSections(): Flow<List<SponsorSection>>

    @Query("SELECT * FROM sponsorSection WHERE episodeUrl = :episodeUrl")
    fun getSponsorSectionsOfEpisode(episodeUrl: String): Flow<List<SponsorSection>>
}