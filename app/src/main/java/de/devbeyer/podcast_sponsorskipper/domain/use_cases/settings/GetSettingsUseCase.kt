package de.devbeyer.podcast_sponsorskipper.domain.use_cases.settings

import de.devbeyer.podcast_sponsorskipper.domain.LocalDataManager
import de.devbeyer.podcast_sponsorskipper.domain.SettingKey
import de.devbeyer.podcast_sponsorskipper.domain.models.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class GetSettingsUseCase(
    private val localDataManager: LocalDataManager,
) {
    operator fun invoke(): Flow<Settings?> = flow {
        val downloadImages = localDataManager.readBooleanSetting(SettingKey.DOWNLOAD_IMAGES).firstOrNull()

        emit(
            Settings(
                downloadImages = downloadImages ?: true,
            )
        )
    }
}