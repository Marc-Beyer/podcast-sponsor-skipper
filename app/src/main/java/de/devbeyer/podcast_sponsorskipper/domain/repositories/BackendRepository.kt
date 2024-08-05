package de.devbeyer.podcast_sponsorskipper.domain.repositories

import androidx.paging.PagingData
import de.devbeyer.podcast_sponsorskipper.domain.models.UserData
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastAndEpisodes
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.models.db.SponsorSection
import kotlinx.coroutines.flow.Flow

interface BackendRepository {
    fun getPodcasts(search: String): Flow<PagingData<PodcastWithRelations>>

    fun searchPodcasts(search: String): Flow<PagingData<PodcastWithRelations>>

    fun getRSSFeed(rssUrl: String): Flow<PodcastAndEpisodes?>

    fun submitSponsorSection(
        episodeUrl: String,
        podcastUrl: String,
        startPosition: Long,
        endPosition: Long,
        duration: Long,
        username: String,
        token: String
    ): Flow<Long?>

    fun register(): Flow<UserData?>

    fun getSponsorSection(
        episodeUrl: String,
        duration: Long
    ): Flow<List<SponsorSection>>

    fun rateSponsorSection(
        sponsorSectionId: Long,
        isPositive: Boolean,
        duration: Long,
        username: String,
        token: String
    ): Flow<Long?>
}