package de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode

import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.FileUseCases

class CompleteEpisodeUseCase(
    private val episodeDao: EpisodeDao,
    private val fileUseCases: FileUseCases,
) {
    suspend operator fun invoke(episode: Episode, podcast: Podcast) {
        episode.episodePath?.let {
            fileUseCases.deleteFileUseCase(it)
        }
        episode.imagePath?.let {
            fileUseCases.deleteFileUseCase(it)
        }
        episodeDao.update(
            episode.copy(
                episodePath = null,
                imagePath = podcast.imagePath,
                isCompleted = true
            )
        )
    }
}