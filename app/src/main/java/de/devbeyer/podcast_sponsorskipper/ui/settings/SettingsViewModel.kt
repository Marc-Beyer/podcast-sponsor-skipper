package de.devbeyer.podcast_sponsorskipper.ui.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val podcastsUseCases: PodcastsUseCases,
    private val workManager: WorkManager,
) : ViewModel() {
    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state


    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.ToggleDownloadEpisodeImages -> {}
        }

    }
}