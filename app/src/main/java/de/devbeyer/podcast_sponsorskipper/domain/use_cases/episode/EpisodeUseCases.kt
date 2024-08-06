package de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode

data class EpisodeUseCases(
    val downloadEpisodeUseCase: DownloadEpisodeUseCase,
    val downloadSponsorSectionsUseCase: DownloadSponsorSectionsUseCase,
    val getSponsorSectionsUseCase: GetSponsorSectionsUseCase,
    val completeEpisodeUseCase: CompleteEpisodeUseCase,
    val markEpisodeCompleteUseCase: MarkEpisodeCompleteUseCase,
    val deleteEpisodeUseCase: DeleteEpisodeUseCase,
    val favoriteEpisodeUseCase: FavoriteEpisodeUseCase,
    val getEpisodeUseCase: GetEpisodeUseCase,
)