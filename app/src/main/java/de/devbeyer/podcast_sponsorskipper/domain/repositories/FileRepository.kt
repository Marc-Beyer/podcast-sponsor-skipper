package de.devbeyer.podcast_sponsorskipper.domain.repositories

import kotlinx.coroutines.flow.Flow

interface FileRepository {
    fun downloadFile(extension: String, url: String, folder: String): Flow<String?>
    fun downloadAudioFile(extension: String, url: String): Flow<String?>
    fun deleteFile(filePath: String): Flow<String?>

    fun downloadImage(
        extension: String,
        url: String,
        folder: String,
        size: Int = 512,
    ): Flow<String?>
}