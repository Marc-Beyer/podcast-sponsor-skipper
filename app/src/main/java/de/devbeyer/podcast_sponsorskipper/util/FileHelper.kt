package de.devbeyer.podcast_sponsorskipper.util

import android.media.MediaMetadataRetriever
import java.io.File

fun getMP3Details(filePath: String): Pair<Long, Long> {
    val file = File(filePath)
    val fileSize = file.length()

    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(filePath)
    val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
    val duration = durationStr?.toLongOrNull() ?: 0L

    retriever.release()

    return Pair(fileSize, duration)
}

fun formatFileSize(sizeInBytes: Long): String {
    val kilobyte = 1024L
    val megabyte = kilobyte * 1024
    val gigabyte = megabyte * 1024

    return when {
        sizeInBytes >= gigabyte -> String.format("%.2f GB", sizeInBytes / gigabyte.toFloat())
        sizeInBytes >= megabyte -> String.format("%.2f MB", sizeInBytes / megabyte.toFloat())
        sizeInBytes >= kilobyte -> String.format("%.2f KB", sizeInBytes / kilobyte.toFloat())
        else -> "$sizeInBytes B"
    }
}

fun removePTags(input: String): String {
    return input.replace(Regex("</?p>"), "")
}