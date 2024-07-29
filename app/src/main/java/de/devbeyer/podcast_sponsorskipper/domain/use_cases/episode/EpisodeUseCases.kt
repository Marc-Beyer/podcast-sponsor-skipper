package de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode

data class EpisodeUseCases(
    val downloadEpisodeUseCase: DownloadEpisodeUseCase,
    val getSponsorSectionsUseCase: GetSponsorSectionsUseCase,
)