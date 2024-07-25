package de.devbeyer.podcast_sponsorskipper.domain.use_cases.file

import androidx.paging.PagingData
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.repositories.FileRepository
import kotlinx.coroutines.flow.Flow

class DownloadFileUseCase(
    private val fileRepository: FileRepository,
) {
    operator fun invoke(extension: String, url: String): Flow<String?> {
        return fileRepository.downloadFile(
            extension = extension,
            url = url
        )
    }
}