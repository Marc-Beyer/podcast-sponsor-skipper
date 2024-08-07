package de.devbeyer.podcast_sponsorskipper.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import de.devbeyer.podcast_sponsorskipper.domain.LocalDataManager
import de.devbeyer.podcast_sponsorskipper.domain.SettingKey
import de.devbeyer.podcast_sponsorskipper.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class LocalDataManagerImpl(
    private val context: Context,
) : LocalDataManager {
    override suspend fun saveCompletedGuidedTour() {
        context.dataStore.edit {
            it[PreferencesKeys.COMPLETED_GUIDED_TOUR] = true
        }
    }

    override fun readCompletedGuidedTour(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[PreferencesKeys.COMPLETED_GUIDED_TOUR] ?: false
        }
    }

    override suspend fun saveUsername(username: String) {
        context.dataStore.edit {
            it[PreferencesKeys.USERNAME] = username
        }
    }

    override fun readUsername(): Flow<String?> {
        return context.dataStore.data.map {
            it[PreferencesKeys.USERNAME]
        }
    }

    override suspend fun saveToken(token: String) {
        context.dataStore.edit {
            it[PreferencesKeys.TOKEN] = token
        }
    }

    override fun readToken(): Flow<String?> {
        return context.dataStore.data.map {
            it[PreferencesKeys.TOKEN]
        }
    }

    override suspend fun saveBooleanSetting(setting: Pair<SettingKey, Boolean>) {
        context.dataStore.edit {
            it[booleanPreferencesKey(setting.first.name)] = setting.second
        }
    }

    override fun readBooleanSetting(settingKey: SettingKey): Flow<Boolean?> {
        return context.dataStore.data.map {
            it[booleanPreferencesKey(settingKey.name)]
        }
    }

    override suspend fun saveIntSetting(setting: Pair<SettingKey, Int>) {
        context.dataStore.edit {
            it[intPreferencesKey(setting.first.name)] = setting.second
        }
    }

    override fun readIntSetting(settingKey: SettingKey): Flow<Int?> {
        return context.dataStore.data.map {
            it[intPreferencesKey(settingKey.name)]
        }
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.USER_PREFERENCES_NAME)

private object PreferencesKeys{
    val COMPLETED_GUIDED_TOUR = booleanPreferencesKey(Constants.COMPLETED_GUIDED_TOUR)
    val USERNAME = stringPreferencesKey(Constants.USERNAME)
    val TOKEN = stringPreferencesKey(Constants.TOKEN)
}
