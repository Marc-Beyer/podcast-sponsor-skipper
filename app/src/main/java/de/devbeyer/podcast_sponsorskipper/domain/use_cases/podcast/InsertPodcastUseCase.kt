package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import de.devbeyer.podcast_sponsorskipper.data.local.dao.CategoryDao
import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.data.local.dao.PodcastDao
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastAndEpisodes
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastCategoryCrossRef
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import kotlinx.coroutines.flow.firstOrNull

class InsertPodcastUseCase(
    private val podcastDao: PodcastDao,
    private val categoryDao: CategoryDao,
    private val episodeDao: EpisodeDao,
) {
    suspend operator fun invoke(podcastAndEpisodes: PodcastAndEpisodes) {
        val podcast = podcastAndEpisodes.podcastWithRelations.podcast.copy(id = 0)
        val podcastId = podcastDao.insert(podcast).toInt()

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
        for (episode in podcastAndEpisodes.episodes){
            episodeDao.insert(episode.copy(podcastId = podcastId))
        }
    }
}