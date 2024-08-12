package de.devbeyer.podcast_sponsorskipper.domain.use_cases.file

data class FileUseCases(
    val downloadFileUseCase: DownloadFileUseCase,
    val deleteFileUseCase: DeleteFileUseCase,
    val streamFileUseCase: StreamFileUseCase,
)
