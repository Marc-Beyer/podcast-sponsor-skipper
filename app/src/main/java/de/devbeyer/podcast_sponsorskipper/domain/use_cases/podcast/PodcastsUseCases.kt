package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

data class PodcastsUseCases(
    val getRemotePodcastsUseCase: GetRemotePodcastsUseCase,
    val getLocalPodcastsUseCase: GetLocalPodcastsUseCase,
    val insertPodcastUseCase: InsertPodcastUseCase,
    val getLocalPodcastByUrl: GetLocalPodcastByUrl,
)