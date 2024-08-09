package de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast

import android.util.Log
import de.devbeyer.podcast_sponsorskipper.domain.repositories.BackendRepository

class RegisterPodcastUseCase(
    private val backendRepository: BackendRepository,
) {
    suspend operator fun invoke(
        url: String,
    ) {
        Log.i("AAA", "RegisterPodcastUseCase")
        backendRepository.registerPodcast(
            url = url,
        )
    }
}