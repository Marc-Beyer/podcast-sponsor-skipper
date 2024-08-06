package de.devbeyer.podcast_sponsorskipper.ui.episode

import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.models.db.SponsorSection
import de.devbeyer.podcast_sponsorskipper.ui.episodes.EpisodeFilter

data class EpisodeState(
    val episode: Episode? = null,
    val podcastWithRelations: PodcastWithRelations? = null,
    val sponsorSections: List<SponsorSection> = emptyList(),
    val activeDownloadUrls: List<String> = emptyList(),

)
