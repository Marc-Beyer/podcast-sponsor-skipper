package de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode

import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.FileUseCases
import kotlinx.coroutines.flow.firstOrNull

class CompleteEpisodeUseCase(
    private val episodeDao: EpisodeDao,
    private val fileUseCases: FileUseCases,
) {
    suspend operator fun invoke(
        episode: Episode,
        podcast: Podcast,
        autoDeleteCompletedEpisodes: Boolean,
    ) {
        val oldImagePath = episode.imagePath

        if(autoDeleteCompletedEpisodes){
            episode.episodePath?.let {
                fileUseCases.deleteFileUseCase(it).firstOrNull()
            }
        }

        episodeDao.update(
            episode.copy(
                episodePath = if(autoDeleteCompletedEpisodes) null else episode.episodePath,
                imagePath = podcast.imagePath,
                isCompleted = true,
                lastPosition = 0,
            )
        )
        oldImagePath?.let {
            val episodesWithThisImage = episodeDao.getEpisodesByImagePath(it).firstOrNull()
            if (episodesWithThisImage.isNullOrEmpty()) {
                fileUseCases.deleteFileUseCase(it).firstOrNull()
            }
        }
    }
}