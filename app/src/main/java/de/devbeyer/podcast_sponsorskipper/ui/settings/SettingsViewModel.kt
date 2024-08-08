package de.devbeyer.podcast_sponsorskipper.ui.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.SettingKey
import de.devbeyer.podcast_sponsorskipper.domain.models.Settings
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

            is SettingsEvent.ResetSettings -> {
                val default = Settings()
                viewModelScope.launch {
                    settingsUseCases.setBooleanSettingUseCase(
                        settingKey = SettingKey.FEED_GRID_LAYOUT,
                        value = default.feedGridLayout,
                    )
                    settingsUseCases.setBooleanSettingUseCase(
                        settingKey = SettingKey.SET_NOTIFICATION_IMAGE,
                        value = default.setNotificationImage,
                    )
                    settingsUseCases.setBooleanSettingUseCase(
                        settingKey = SettingKey.ENABLE_MARQUEE,
                        value = default.enableMarquee,
                    )
                    settingsUseCases.setBooleanSettingUseCase(
                        settingKey = SettingKey.ENABLE_MARQUEE_IN_PLAYER,
                        value = default.enableMarqueeInPlayer,
                    )


                    settingsUseCases.setBooleanSettingUseCase(
                        settingKey = SettingKey.DOWNLOAD_IMAGES,
                        value = default.downloadImages,
                    )
                    settingsUseCases.setBooleanSettingUseCase(
                        settingKey = SettingKey.AUTO_DELETE_COMPLETED_EPISODES,
                        value = default.autoDeleteCompletedEpisodes,
                    )

                    settingsUseCases.setIntSettingUseCase(
                        settingKey = SettingKey.FORWARD_TIME,
                        value = default.forwardTime,
                    )
                    settingsUseCases.setIntSettingUseCase(
                        settingKey = SettingKey.REWIND_TIME,
                        value = default.rewindTime,
                    )

                    event.callback()
                    setSettings()
                }
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