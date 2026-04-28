package com.h2grow.app.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OnboardingPreferencesRepository @Inject constructor(
    private val authDataStore: DataStore<Preferences>
) {
    val isOnboardingCompletedFlow: Flow<Boolean> = authDataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
        }

    suspend fun setOnboardingCompleted(isCompleted: Boolean) {
        authDataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] = isCompleted
        }
    }
}
