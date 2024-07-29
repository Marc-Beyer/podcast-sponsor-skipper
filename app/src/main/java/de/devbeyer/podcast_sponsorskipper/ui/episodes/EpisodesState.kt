package de.devbeyer.podcast_sponsorskipper.ui.episodes

import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations

data class EpisodesState (
    val podcastWithRelations: PodcastWithRelations? = null,
    val episodes:List<Episode> = emptyList(),
    val activeDownloadUrls: List<String> = emptyList(),
)
