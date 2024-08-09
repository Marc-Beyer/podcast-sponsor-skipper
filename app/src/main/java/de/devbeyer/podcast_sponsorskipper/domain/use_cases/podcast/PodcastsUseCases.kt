package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

data class PodcastsUseCases(
    val getRemotePodcastsUseCase: GetRemotePodcastsUseCase,
    val getLocalPodcastsUseCase: GetLocalPodcastsUseCase,
    val insertPodcastUseCase: InsertPodcastUseCase,
    val deleteLocalPodcastUseCase: DeleteLocalPodcastUseCase,
    val getLocalPodcastByUrlUseCase: GetLocalPodcastByUrlUseCase,
    val getRSSFeedUseCase: GetRSSFeedUseCase,
    val getEpisodesOfPodcastUseCase: GetEpisodesOfPodcastUseCase,
    val submitSponsorSectionUseCase: SubmitSponsorSectionUseCase,
    val rateSponsorSectionUseCase: RateSponsorSectionUseCase,
    val getLocalPodcastByIdUseCase: GetLocalPodcastByIdUseCase,
    val registerPodcastUseCase: RegisterPodcastUseCase,
)