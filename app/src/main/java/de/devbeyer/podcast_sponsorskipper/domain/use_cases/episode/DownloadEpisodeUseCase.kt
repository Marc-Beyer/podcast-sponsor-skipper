package de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode

import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.FileUseCases
import kotlinx.coroutines.flow.firstOrNull

class DownloadEpisodeUseCase(
    private val episodeDao: EpisodeDao,
    private val fileUseCases: FileUseCases,
) {
    suspend operator fun invoke(episodeUrl: String) {
        val episodePath = fileUseCases.downloadFileUseCase(
            extension = "mp3",
            url = episodeUrl,
            folder = "audio",
        ).firstOrNull()
        if(episodePath != null){
            try {
                val rowsUpdated = episodeDao.updateEpisodePath(episodeUrl = episodeUrl, episodePath = episodePath)
                if(rowsUpdated == 0) throw Exception()
            }catch (e: Exception){
                e.printStackTrace()
                fileUseCases.deleteFileUseCase(filePath = episodePath).firstOrNull()
            }
        }
    }
}