package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

data class PodcastsUseCases(
    val getRemotePodcastsUseCase: GetRemotePodcastsUseCase,
    val getLocalPodcastsUseCase: GetLocalPodcastsUseCase,
    val insertPodcastUseCase: InsertPodcastUseCase,
    val deleteLocalPodcastUseCase: DeleteLocalPodcastUseCase,
    val getLocalPodcastByUrl: GetLocalPodcastByUrl,
    val getRSSFeed: GetRSSFeed,
    val getEpisodesOfPodcastUseCase: GetEpisodesOfPodcastUseCase,
    val submitSponsorSectionUseCase: SubmitSponsorSectionUseCase,
)