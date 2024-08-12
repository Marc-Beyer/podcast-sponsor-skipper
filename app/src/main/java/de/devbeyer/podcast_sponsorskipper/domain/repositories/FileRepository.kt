package de.devbeyer.podcast_sponsorskipper.domain.repositories

import kotlinx.coroutines.flow.Flow

interface FileRepository {
    fun downloadFile(extension: String, url: String, folder: String): Flow<String?>
    fun downloadAudioFile(extension: String, url: String): Flow<String?>
    fun deleteFile(filePath: String): Flow<String?>

    suspend fun streamFile(url: String, extension: String, folder: String): String
}