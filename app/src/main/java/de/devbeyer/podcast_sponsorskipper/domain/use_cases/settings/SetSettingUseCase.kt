package de.devbeyer.podcast_sponsorskipper.domain.use_cases.settings

import de.devbeyer.podcast_sponsorskipper.domain.LocalDataManager
import de.devbeyer.podcast_sponsorskipper.domain.SettingKey

class SetSettingUseCase(
    private val localDataManager: LocalDataManager,
) {
    suspend operator fun invoke(settingKey: SettingKey, value: Boolean) {
        localDataManager.saveBooleanSetting(setting = Pair(settingKey, value))
    }
}