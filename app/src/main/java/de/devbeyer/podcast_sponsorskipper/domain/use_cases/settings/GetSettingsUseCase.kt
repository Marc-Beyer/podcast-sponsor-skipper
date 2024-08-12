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
        val downloadImages =
            localDataManager.readBooleanSetting(SettingKey.DOWNLOAD_IMAGES).firstOrNull()
        val feedGridLayout =
            localDataManager.readBooleanSetting(SettingKey.FEED_GRID_LAYOUT).firstOrNull()
        val setNotificationImage =
            localDataManager.readBooleanSetting(SettingKey.SET_NOTIFICATION_IMAGE).firstOrNull()
        val autoDeleteCompletedEpisodes =
            localDataManager.readBooleanSetting(SettingKey.AUTO_DELETE_COMPLETED_EPISODES)
                .firstOrNull()
        val enableMarquee =
            localDataManager.readBooleanSetting(SettingKey.ENABLE_MARQUEE).firstOrNull()
        val enableMarqueeInPlayer =
            localDataManager.readBooleanSetting(SettingKey.ENABLE_MARQUEE_IN_PLAYER).firstOrNull()

        val coverImageSize = localDataManager.readIntSetting(SettingKey.COVER_IMG_SIZE).firstOrNull()
        val rewindTime = localDataManager.readIntSetting(SettingKey.REWIND_TIME).firstOrNull()
        val forwardTime = localDataManager.readIntSetting(SettingKey.FORWARD_TIME).firstOrNull()

        val defaultSettings = Settings()

        emit(
            Settings(
                feedGridLayout = feedGridLayout ?: defaultSettings.feedGridLayout,
                setNotificationImage = setNotificationImage ?: defaultSettings.setNotificationImage,
                enableMarquee = enableMarquee ?: defaultSettings.enableMarquee,
                enableMarqueeInPlayer = enableMarqueeInPlayer ?: defaultSettings.enableMarqueeInPlayer,

                downloadImages = downloadImages ?: defaultSettings.downloadImages,
                coverImageSize = coverImageSize ?: defaultSettings.coverImageSize,
                autoDeleteCompletedEpisodes = autoDeleteCompletedEpisodes ?: defaultSettings.autoDeleteCompletedEpisodes,

                forwardTime = forwardTime ?: defaultSettings.forwardTime,
                rewindTime = rewindTime ?: defaultSettings.rewindTime,
            )
        )
    }
}