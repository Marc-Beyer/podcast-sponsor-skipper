package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import android.util.Log
import de.devbeyer.podcast_sponsorskipper.data.local.dao.CategoryDao
import de.devbeyer.podcast_sponsorskipper.data.local.dao.PodcastDao
import de.devbeyer.podcast_sponsorskipper.domain.models.Category
import de.devbeyer.podcast_sponsorskipper.domain.models.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.models.PodcastCategoryCrossRef
import de.devbeyer.podcast_sponsorskipper.domain.models.PodcastWithRelations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class InsertPodcastUseCase(
    private val podcastDao: PodcastDao,
    private val categoryDao: CategoryDao,
) {
    suspend operator fun invoke(podcastWithRelations: PodcastWithRelations) {
        val podcast = podcastWithRelations.podcast.copy(id = 0)
        podcastDao.insert(podcast)

        for (categoryToAdd in podcastWithRelations.categories) {
            Log.i("AAA", categoryToAdd.name)
            val category = categoryDao.getCategory(categoryToAdd.name)
                .firstOrNull()
                ?: categoryToAdd.copy(id = 0).also {
                    categoryDao.insert(it)
                }

            podcastDao.insertPodcastCategoryCrossRef(
                PodcastCategoryCrossRef(
                    podcastId = podcast.id,
                    categoryId = category.id
                )
            )
        }
    }
}