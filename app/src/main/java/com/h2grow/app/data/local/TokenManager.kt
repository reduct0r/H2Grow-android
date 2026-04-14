package com.h2grow.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenManager (
    private val context: Context
) {
    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.authDataStore.edit { preferences ->
            preferences[PreferencesKeys.JWT_ACCESS_TOKEN] = accessToken
            preferences[PreferencesKeys.JWT_REFRESH_TOKEN] = refreshToken
        }
    }

    val accessTokenFlow: Flow<String?> = context.authDataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.JWT_ACCESS_TOKEN]
        }

    val refreshTokenFlow: Flow<String?> = context.authDataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.JWT_REFRESH_TOKEN]
        }


    suspend fun clearTokens() {
        context.authDataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun isLoggedIn() {}

}