package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.data.local.dao.PodcastDao
import de.devbeyer.podcast_sponsorskipper.data.worker.DownloadManager
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.FileUseCases
import kotlinx.coroutines.flow.firstOrNull

class DeleteLocalPodcastUseCase(
    private val podcastDao: PodcastDao,
    private val episodeDao: EpisodeDao,
    private val fileUseCases: FileUseCases
) {
    suspend operator fun invoke(podcast: Podcast) {
        episodeDao.getEpisodesByPodcastId(podcast.id).firstOrNull()?.forEach { episode ->
            episode.imagePath?.let { imagePath ->
                val returnedFilePath = fileUseCases.deleteFileUseCase(imagePath).firstOrNull()
            }
            episode.episodePath?.let { episodePath ->
                val returnedFilePath = fileUseCases.deleteFileUseCase(episodePath).firstOrNull()
            }
            DownloadManager.cancel(
                title = episode.title,
                url = episode.episodeUrl,
            )
        }
        podcast.imagePath?.let {
            val returnedFilePath = fileUseCases.deleteFileUseCase(it).firstOrNull()
        }


        podcastDao.deletePodcast(podcast)
    }
}