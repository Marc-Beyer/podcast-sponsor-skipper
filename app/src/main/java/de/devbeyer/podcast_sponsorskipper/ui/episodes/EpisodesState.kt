package de.devbeyer.podcast_sponsorskipper.ui.episodes

import androidx.compose.ui.unit.DpOffset
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations

data class EpisodesState(
    val podcastWithRelations: PodcastWithRelations? = null,
    val episodes: List<Episode> = emptyList(),
    val activeDownloadUrls: List<String> = emptyList(),
    val selectedEpisode: Episode? = null,
    val menuOffset: DpOffset = DpOffset.Zero,
    val isMenuExpanded: Boolean = false,
    val activeFilter: EpisodeFilter = EpisodeFilter.ALL,
    val isFilterMenuExpanded: Boolean = false,

    )
