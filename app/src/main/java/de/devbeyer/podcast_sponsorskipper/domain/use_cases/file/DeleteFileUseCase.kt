package de.devbeyer.podcast_sponsorskipper.domain.use_cases.file

import android.util.Log
import de.devbeyer.podcast_sponsorskipper.domain.repositories.FileRepository
import kotlinx.coroutines.flow.Flow

class DeleteFileUseCase(
    private val fileRepository: FileRepository,
) {
    operator fun invoke(filePath: String): Flow<String?> {
        Log.i("AAA", "invoke $filePath")
        val deletedFilePath = fileRepository.deleteFile(filePath)
        Log.i("AAA", "deletedFilePath $deletedFilePath")
        return deletedFilePath
    }
}