package de.devbeyer.podcast_sponsorskipper.util

import okhttp3.ResponseBody
import java.io.File

fun ResponseBody.saveFile(
    file: File,
) {
    byteStream().use { inputStream ->
        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
}