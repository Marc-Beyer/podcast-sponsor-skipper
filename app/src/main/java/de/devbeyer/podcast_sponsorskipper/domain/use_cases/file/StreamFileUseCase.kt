package de.devbeyer.podcast_sponsorskipper.domain.use_cases.file

import de.devbeyer.podcast_sponsorskipper.domain.repositories.FileRepository

class StreamFileUseCase(
    private val fileRepository: FileRepository,
) {
    suspend operator fun invoke(extension: String, url: String, folder: String): String {
        return fileRepository.streamFile(
            extension = extension,
            url = url,
            folder = folder
        )
    }
}