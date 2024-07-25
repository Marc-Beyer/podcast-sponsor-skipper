package de.devbeyer.podcast_sponsorskipper.data.repositories

import android.content.Context
import de.devbeyer.podcast_sponsorskipper.data.remote.BackendAPI
import de.devbeyer.podcast_sponsorskipper.data.remote.FileAPI
import de.devbeyer.podcast_sponsorskipper.data.remote.RSSAPI
import de.devbeyer.podcast_sponsorskipper.domain.repositories.FileRepository
import de.devbeyer.podcast_sponsorskipper.domain.repositories.PodcastRepository
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.FileUseCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

class FileRepositoryImpl(
    private val fileAPI: FileAPI,
    private val context: Context,
): FileRepository {
    override fun downloadFile(extension: String, url: String): Flow<String?> = flow {
        try {
            val response = fileAPI.downloadFile(url)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    val imageDir = File(context.filesDir, "images")
                    if (!imageDir.exists()) {
                        imageDir.mkdir()
                    }

                    var filename: String
                    var file: File

                    do {
                        filename = "${UUID.randomUUID()}.${extension}"
                        file = File(imageDir, filename)
                    } while (file.exists())

                    val inputStream: InputStream = responseBody.byteStream()
                    val outputStream = FileOutputStream(file)

                    val buffer = ByteArray(4 * 1024)
                    var read: Int
                    while (inputStream.read(buffer).also { read = it } != -1) {
                        outputStream.write(buffer, 0, read)
                    }

                    outputStream.flush()
                    outputStream.close()
                    inputStream.close()

                    emit(file.absolutePath)
                } else {
                    emit(null)
                }
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(null)
        }
    }
}