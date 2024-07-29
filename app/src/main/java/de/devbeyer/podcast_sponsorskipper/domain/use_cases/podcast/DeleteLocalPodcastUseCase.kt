package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import android.util.Log
import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.data.local.dao.PodcastDao
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.FileUseCases
import kotlinx.coroutines.flow.firstOrNull

class DeleteLocalPodcastUseCase(
    private val podcastDao: PodcastDao,
    private val episodeDao: EpisodeDao,
    private val fileUseCases: FileUseCases
) {
    suspend operator fun invoke(podcast: Podcast) {
        Log.i("AAA", "Delete Podcast ${podcast.title}")
        episodeDao.getEpisodesByPodcastId(podcast.id).firstOrNull()?.forEach { episode ->
            episode.imagePath?.let { imagePath ->
                val returnedFilePath = fileUseCases.deleteFileUseCase(imagePath).firstOrNull()
                Log.i("AAA", "DELETED $returnedFilePath")
            }
            episode.episodePath?.let { episodePath ->
                val returnedFilePath = fileUseCases.deleteFileUseCase(episodePath).firstOrNull()
                Log.i("AAA", "DELETED $returnedFilePath")
            }
        }
        podcast.imagePath?.let {
            val returnedFilePath = fileUseCases.deleteFileUseCase(it).firstOrNull()
            Log.i("AAA", "DELETED $returnedFilePath")
        }


        podcastDao.deletePodcast(podcast)
    }
}