package de.devbeyer.podcast_sponsorskipper.domain.use_cases.settings

import de.devbeyer.podcast_sponsorskipper.domain.LocalDataManager
import de.devbeyer.podcast_sponsorskipper.domain.SettingKey

class SetIntSettingUseCase(
    private val localDataManager: LocalDataManager,
) {
    suspend operator fun invoke(settingKey: SettingKey, value: Int) {
        localDataManager.saveIntSetting(setting = Pair(settingKey, value))
    }
}