package de.devbeyer.podcast_sponsorskipper.domain.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "category")
data class Category(
    @PrimaryKey
    @ColumnInfo(name = "categoryId")
    val id: Int,
    val name: String,
) : Parcelable
