package de.devbeyer.podcast_sponsorskipper.data.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import de.devbeyer.podcast_sponsorskipper.data.remote.FileAPI
import de.devbeyer.podcast_sponsorskipper.domain.repositories.FileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.ByteArrayOutputStream
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
            if (file.exists()) {
                val deleted = file.delete()
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


    override fun downloadImage(
        extension: String,
        url: String,
        folder: String,
        size: Int,
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
                    val byteArrayOutputStream = ByteArrayOutputStream()

                    val buffer = ByteArray(4 * 1024)
                    var read: Int
                    while (inputStream.read(buffer).also { read = it } != -1) {
                        byteArrayOutputStream.write(buffer, 0, read)
                    }

                    val byteArray = byteArrayOutputStream.toByteArray()
                    val originalBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

                    val width = originalBitmap.width
                    val height = originalBitmap.height
                    val newSize = minOf(width, height)

                    val cropX = (width - newSize) / 2
                    val cropY = (height - newSize) / 2

                    val croppedBitmap = Bitmap.createBitmap(
                        originalBitmap, cropX, cropY, newSize, newSize
                    )

                    val resizedBitmap = Bitmap.createScaledBitmap(
                        croppedBitmap,
                        minOf(size, newSize),
                        minOf(size, newSize),
                        true
                    )

                    val outputStream = FileOutputStream(file)
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                    outputStream.flush()
                    outputStream.close()

                    inputStream.close()
                    byteArrayOutputStream.close()
                    originalBitmap.recycle()
                    croppedBitmap.recycle()
                    resizedBitmap.recycle()

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