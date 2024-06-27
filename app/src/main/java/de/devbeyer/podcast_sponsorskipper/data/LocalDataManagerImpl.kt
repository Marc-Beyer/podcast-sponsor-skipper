package de.devbeyer.podcast_sponsorskipper.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import de.devbeyer.podcast_sponsorskipper.domain.LocalDataManager
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
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.USER_PREFERENCES_NAME)

private object PreferencesKeys{
    val COMPLETED_GUIDED_TOUR = booleanPreferencesKey(Constants.COMPLETED_GUIDED_TOUR)
}
