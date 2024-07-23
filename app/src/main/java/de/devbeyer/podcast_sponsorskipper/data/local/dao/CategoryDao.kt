package de.devbeyer.podcast_sponsorskipper.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.devbeyer.podcast_sponsorskipper.domain.models.Category

@Dao
interface  CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Query("SELECT * FROM category")
    fun getAllLive(): LiveData<List<Category>>

    @Query("SELECT * FROM category WHERE categoryId = :categoryId")
    fun getCategoryLive(categoryId: String): LiveData<Category>

    @Query("SELECT * FROM category WHERE categoryId = :categoryId")
    fun getCategory(categoryId: String): Category?
}