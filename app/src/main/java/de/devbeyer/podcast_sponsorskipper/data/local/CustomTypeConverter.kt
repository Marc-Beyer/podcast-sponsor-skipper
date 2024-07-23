package de.devbeyer.podcast_sponsorskipper.data.local

import androidx.room.TypeConverter
import java.time.LocalDateTime

class CustomTypeConverter {
    @TypeConverter
    fun stringToLocalDate(str: String?): LocalDateTime? {
        return str?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun localDateToSting(date: LocalDateTime?): String? {
        return date?.toString()
    }
}