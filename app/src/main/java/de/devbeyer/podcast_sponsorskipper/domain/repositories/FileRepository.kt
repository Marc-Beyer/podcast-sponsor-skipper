package de.devbeyer.podcast_sponsorskipper.domain.repositories

import android.content.Context
import androidx.paging.PagingData
import de.devbeyer.podcast_sponsorskipper.data.remote.FileAPI
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastAndEpisodes
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import kotlinx.coroutines.flow.Flow

interface FileRepository {
    fun downloadFile(extension: String, url: String): Flow<String?>

}