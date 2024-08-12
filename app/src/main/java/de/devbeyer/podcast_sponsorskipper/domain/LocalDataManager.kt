package de.devbeyer.podcast_sponsorskipper.domain

import kotlinx.coroutines.flow.Flow


interface LocalDataManager {
    suspend fun saveCompletedGuidedTour()
    fun readCompletedGuidedTour(): Flow<Boolean>
    suspend fun saveUsername(username: String)
    fun readUsername(): Flow<String?>
    suspend fun saveToken(token: String)
    fun readToken(): Flow<String?>

    suspend fun saveBooleanSetting(setting: Pair<SettingKey, Boolean>)
    fun readBooleanSetting(settingKey: SettingKey): Flow<Boolean?>
    suspend fun saveIntSetting(setting: Pair<SettingKey, Int>)
    fun readIntSetting(settingKey: SettingKey): Flow<Int?>
}

enum class SettingKey{
    DOWNLOAD_IMAGES,
    FEED_GRID_LAYOUT,
    SET_NOTIFICATION_IMAGE,
    FORWARD_TIME,
    REWIND_TIME,
    AUTO_DELETE_COMPLETED_EPISODES,
    ENABLE_MARQUEE,
    ENABLE_MARQUEE_IN_PLAYER,
    COVER_IMG_SIZE,
}