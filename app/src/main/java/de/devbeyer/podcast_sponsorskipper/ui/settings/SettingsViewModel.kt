package de.devbeyer.podcast_sponsorskipper.ui.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.settings.SettingsUseCases
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val podcastsUseCases: PodcastsUseCases,
    private val workManager: WorkManager,
    private val settingsUseCases: SettingsUseCases,
) : ViewModel() {
    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state

    init {
        setSettings()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ChangeForwardTimeInputValue -> {
                _state.value = state.value.copy(
                    forwardTimeInputValue = event.value
                )
            }

            is SettingsEvent.ChangeRewindTimeInputValue -> {
                _state.value = state.value.copy(
                    rewindTimeInputValue = event.value
                )
            }

            SettingsEvent.UpdateSettings -> {
                setSettings()
            }
        }
    }

    private fun setSettings() {
        viewModelScope.launch {
            settingsUseCases.getSettingsUseCase().firstOrNull()?.let { settings ->
                _state.value = state.value.copy(
                    forwardTimeInputValue = settings.forwardTime.toString(),
                    rewindTimeInputValue = settings.rewindTime.toString(),
                )
            }
        }
    }
}