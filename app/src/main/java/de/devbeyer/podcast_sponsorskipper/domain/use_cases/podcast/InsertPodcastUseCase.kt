package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import android.util.Log
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
    ) {
        val podcastWithRelations =
            podcastDao.getPodcastFromUrl(podcastAndEpisodes.podcastWithRelations.podcast.url)
                .firstOrNull()
        var podcastId = 0
        var podcastImagePath = ""
        if (podcastWithRelations == null) {
            Log.i("AAA", "Podcast does not exists!")
            podcastImagePath = fileUseCases.downloadFileUseCase.invoke(
                extension = "jpg",
                url = podcastAndEpisodes.podcastWithRelations.podcast.imageUrl
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
            Log.i("AAA", "Podcast already exists!")
            podcastId = podcastWithRelations.podcast.id
            podcastImagePath = podcastWithRelations.podcast.imagePath ?: ""
        }

        val imageCache = mutableMapOf<String, String?>()

        for (episode in podcastAndEpisodes.episodes) {
            Log.i("AAA", "ADD episode ${episode.title}")

            val foundEpisode = episodeDao.getEpisodeByUrl(episode.episodeUrl).firstOrNull()
            if (foundEpisode == null) {
                val episodeImagePath = if (downloadImages) {
                    imageCache[episode.imageUrl] ?: fileUseCases.downloadFileUseCase.invoke(
                        extension = "jpg",
                        url = episode.imageUrl
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

                imageCache[episode.imageUrl] = episodeImagePath
                episodeDao.insert(episode.copy(podcastId = podcastId, imagePath = episodeImagePath))
            } else if (foundEpisode.imagePath != null) {
                imageCache[foundEpisode.imageUrl] = foundEpisode.imagePath
            }
        }
    }
}