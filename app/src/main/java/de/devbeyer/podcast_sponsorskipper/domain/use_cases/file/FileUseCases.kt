package de.devbeyer.podcast_sponsorskipper.domain.use_cases.file

import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.GetRemotePodcastsUseCase

data class FileUseCases(
    val downloadFileUseCase: DownloadFileUseCase,
)
