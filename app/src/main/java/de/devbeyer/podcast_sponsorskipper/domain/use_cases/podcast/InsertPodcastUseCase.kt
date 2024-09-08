package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import de.devbeyer.podcast_sponsorskipper.data.local.dao.CategoryDao
import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.data.local.dao.PodcastDao
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastAndEpisodes
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastCategoryCrossRef
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.FileUseCases
import kotlinx.coroutines.flow.firstOrNull

class InsertPodcastUseCase(
    private val podcastDao: PodcastDao,
    private val categoryDao: CategoryDao,
    private val episodeDao: EpisodeDao,
    private val fileUseCases: FileUseCases,
) {
    suspend operator fun invoke(
        podcastAndEpisodes: PodcastAndEpisodes,
        downloadImages: Boolean,
        coverImageSize: Int = 512,
    ): Int {
        val podcastWithRelations =
            podcastDao.getPodcastFromUrl(podcastAndEpisodes.podcastWithRelations.podcast.url)
                .firstOrNull()
        var podcastId = 0
        var podcastImagePath = ""
        var newEpisodes = 0

        if (podcastWithRelations == null) {
            podcastImagePath = fileUseCases.downloadImageUseCase.invoke(
                extension = "jpg",
                url = podcastAndEpisodes.podcastWithRelations.podcast.imageUrl,
                size = coverImageSize,
            ).firstOrNull() ?: ""
            val podcast = podcastAndEpisodes.podcastWithRelations.podcast.copy(
                id = 0,
                imagePath = podcastImagePath
            )
            podcastId = podcastDao.insert(podcast).toInt()

            for (categoryToAdd in podcastAndEpisodes.podcastWithRelations.categories) {
                var categoryId = -1
                val category = categoryDao.getCategory(categoryToAdd.name)
                    .firstOrNull()
                    ?: categoryToAdd.copy(id = 0).also {
                        categoryId = categoryDao.insert(it).toInt()
                    }

                if (categoryId == -1) categoryId = category.id

                podcastDao.insertPodcastCategoryCrossRef(
                    PodcastCategoryCrossRef(
                        podcastId = podcastId,
                        categoryId = categoryId
                    )
                )
            }
        } else {
            podcastId = podcastWithRelations.podcast.id
            podcastImagePath = podcastWithRelations.podcast.imagePath ?: ""
        }

        val imageCache = mutableMapOf<String, String?>()

        for (episode in podcastAndEpisodes.episodes) {

            val foundEpisode = episodeDao.getEpisodeByUrl(episode.episodeUrl).firstOrNull()
            if (foundEpisode == null) {
                newEpisodes++
                val episodeImagePath = if (downloadImages && episode.imageUrl.isNotBlank()) {
                    imageCache[episode.imageUrl] ?: fileUseCases.downloadImageUseCase.invoke(
                        extension = "jpg",
                        url = episode.imageUrl,
                        size = coverImageSize,
                    ).firstOrNull().let { downloadedPath ->
                        if (!downloadedPath.isNullOrBlank()) {
                            downloadedPath
                        } else {
                            podcastImagePath
                        }
                    }
                } else {
                    podcastImagePath
                }
                try {
                    episodeDao.insert(
                        episode.copy(
                            podcastId = podcastId,
                            imagePath = episodeImagePath
                        )
                    )
                    imageCache[episode.imageUrl] = episodeImagePath
                } catch (e: Exception) {
                    fileUseCases.deleteFileUseCase(episodeImagePath).firstOrNull()
                    throw e
                }
            } else if (foundEpisode.imagePath != null) {
                imageCache[foundEpisode.imageUrl] = foundEpisode.imagePath
            }
        }

        return newEpisodes
    }
}