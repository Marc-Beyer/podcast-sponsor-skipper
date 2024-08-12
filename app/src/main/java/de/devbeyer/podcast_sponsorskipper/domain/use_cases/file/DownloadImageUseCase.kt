package de.devbeyer.podcast_sponsorskipper.domain.use_cases.file

import de.devbeyer.podcast_sponsorskipper.domain.repositories.FileRepository
import kotlinx.coroutines.flow.Flow


class DownloadImageUseCase(
    private val fileRepository: FileRepository,
) {
    operator fun invoke(extension: String, url: String, folder:String = "images", size: Int = 512): Flow<String?> {
        return fileRepository.downloadImage(
            extension = extension,
            url = url,
            folder = folder,
            size = size,
        )
    }
}