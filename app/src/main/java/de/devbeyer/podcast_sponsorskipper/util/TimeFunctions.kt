package de.devbeyer.podcast_sponsorskipper.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun getCurrentISO8601Time(): String {
    val currentInstant = Instant.now()
    val formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC)
    return formatter.format(currentInstant)
}

fun formatDateByDistance(dateTime: LocalDateTime): String {
    val currentDateTime = LocalDateTime.now()
    val duration = ChronoUnit.MINUTES.between(dateTime, currentDateTime)

    return when {
        duration < 0 -> "In the future"
        duration < 60 * 24 -> dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        duration < 60 * 24 * 7 -> "${duration / (60 * 24)}d"
        duration < 60 * 24 * 30 -> "${duration / (60 * 24 * 7)}w"
        duration < 60 * 24 * 365 -> "${duration / (60 * 24 * 30)}m"
        else -> "${duration / (60 * 24 * 365)}y"
    }
}

fun formatLocalDateTime(dateTime: LocalDateTime, pattern: String = "yyyy/MM/dd"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return dateTime.format(formatter)
}

fun formatMillisecondsToTime(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}