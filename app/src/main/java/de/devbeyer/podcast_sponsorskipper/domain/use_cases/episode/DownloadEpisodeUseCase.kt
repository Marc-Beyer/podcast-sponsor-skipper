package de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode

import android.util.Log
import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.FileUseCases
import kotlinx.coroutines.flow.firstOrNull

class DownloadEpisodeUseCase(
    private val episodeDao: EpisodeDao,
    private val fileUseCases: FileUseCases,
) {
    suspend operator fun invoke(episodeUrl: String) {
        Log.i("AAA", "Download $episodeUrl")
        val episodePath = fileUseCases.downloadFileUseCase(
            extension = "mp3",
            url = episodeUrl,
            folder = "audio",
        ).firstOrNull()
        Log.i("AAA", "Downloaded $episodePath")
        if(episodePath != null){
            episodeDao.updateEpisodePath(episodeUrl = episodeUrl, episodePath = episodePath)
        }
    }
}