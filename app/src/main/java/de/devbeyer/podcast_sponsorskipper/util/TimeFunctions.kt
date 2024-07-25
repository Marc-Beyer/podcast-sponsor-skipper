package de.devbeyer.podcast_sponsorskipper.util

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun getCurrentISO8601Time(): String {
    val currentInstant = Instant.now()
    val formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC)
    return formatter.format(currentInstant)
}