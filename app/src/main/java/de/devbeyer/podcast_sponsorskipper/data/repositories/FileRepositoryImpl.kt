package de.devbeyer.podcast_sponsorskipper.data.repositories

import android.content.Context
import android.util.Log
import de.devbeyer.podcast_sponsorskipper.data.remote.FileAPI
import de.devbeyer.podcast_sponsorskipper.domain.repositories.FileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

class FileRepositoryImpl(
    private val fileAPI: FileAPI,
    private val context: Context,
) : FileRepository {

    override fun deleteFile(filePath: String): Flow<String?> = flow {
        try {
            val file = File(filePath)
            Log.i("AAA", "filePath $filePath")
            if (file.exists()) {
                Log.i("AAA", "exists $filePath")
                val deleted = file.delete()
                Log.i("AAA", "delete $deleted $filePath")
                if (deleted) {
                    emit(filePath)
                } else {
                    emit(null)
                }
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun downloadFile(
        extension: String,
        url: String,
        folder: String,
    ): Flow<String?> = flow {
        try {
            val response = fileAPI.downloadFile(url)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    val imageDir = File(context.filesDir, folder)
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
        }
    }

    override fun downloadAudioFile(extension: String, url: String): Flow<String?> = flow {
        emit("TODO")
    }
}