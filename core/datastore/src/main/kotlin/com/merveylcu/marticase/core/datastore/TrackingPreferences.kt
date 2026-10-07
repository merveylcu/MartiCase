package com.merveylcu.marticase.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** Whether the user has tracking turned on. Survives process death (ADR 0004, 0006). */
@Singleton
public class TrackingPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    public val isTracking: Flow<Boolean> = dataStore.data.map { it[IS_TRACKING] ?: false }

    public suspend fun setTracking(isTracking: Boolean) {
        dataStore.edit { it[IS_TRACKING] = isTracking }
    }

    private companion object {
        val IS_TRACKING = booleanPreferencesKey("is_tracking")
    }
}
