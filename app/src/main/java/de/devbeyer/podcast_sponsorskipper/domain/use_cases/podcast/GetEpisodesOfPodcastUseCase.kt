package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class GetEpisodesOfPodcastUseCase(
    private val episodeDao: EpisodeDao,
) {
    @OptIn(FlowPreview::class)
    operator fun invoke(podcastId: Int): Flow<List<Episode>> {
        return episodeDao.getEpisodesByPodcastId(podcastId).distinctUntilChanged()
    }
}