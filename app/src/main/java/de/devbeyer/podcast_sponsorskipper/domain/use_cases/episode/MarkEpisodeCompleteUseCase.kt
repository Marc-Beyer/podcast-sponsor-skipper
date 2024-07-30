package de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode

import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.data.local.dao.PodcastDao
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.FileUseCases
import kotlinx.coroutines.flow.firstOrNull

class MarkEpisodeCompleteUseCase(
    private val episodeDao: EpisodeDao,
    private val podcastsDao: PodcastDao,
    private val fileUseCases: FileUseCases,
) {
    suspend operator fun invoke(episode: Episode) {
        if (episode.isCompleted) {
            episodeDao.update(
                episode.copy(
                    isCompleted = false,
                )
            )
        } else {
            val oldImagePath = episode.imagePath
            val imagePath =
                podcastsDao.getPodcastFromId(episode.podcastId).firstOrNull()?.podcast?.imagePath
            episodeDao.update(
                episode.copy(
                    imagePath = imagePath,
                    isCompleted = true,
                )
            )
            oldImagePath?.let {
                val episodesWithThisImage = episodeDao.getEpisodesByImagePath(it).firstOrNull()
                if(episodesWithThisImage.isNullOrEmpty()){
                    fileUseCases.deleteFileUseCase(it).firstOrNull()
                }
            }
        }
    }
}