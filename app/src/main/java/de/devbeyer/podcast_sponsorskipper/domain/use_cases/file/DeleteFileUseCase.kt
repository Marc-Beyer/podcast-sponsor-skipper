package de.devbeyer.podcast_sponsorskipper.domain.use_cases.file

import de.devbeyer.podcast_sponsorskipper.domain.repositories.FileRepository
import kotlinx.coroutines.flow.Flow

class DeleteFileUseCase(
    private val fileRepository: FileRepository,
) {
    operator fun invoke(filePath: String): Flow<String?> {
        val deletedFilePath = fileRepository.deleteFile(filePath)
        return deletedFilePath
    }
}