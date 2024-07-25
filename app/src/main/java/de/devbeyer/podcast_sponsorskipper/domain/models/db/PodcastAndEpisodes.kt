package de.devbeyer.podcast_sponsorskipper.domain.models.db

data class PodcastAndEpisodes(
    val podcastWithRelations: PodcastWithRelations,
    val episodes: List<Episode>,
)