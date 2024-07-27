package de.devbeyer.podcast_sponsorskipper.domain.use_cases.file

import de.devbeyer.podcast_sponsorskipper.domain.repositories.FileRepository
import kotlinx.coroutines.flow.Flow

class DownloadFileUseCase(
    private val fileRepository: FileRepository,
) {
    operator fun invoke(extension: String, url: String, folder:String = "images"): Flow<String?> {
        return fileRepository.downloadFile(
            extension = extension,
            url = url,
            folder = folder
        )
    }
}