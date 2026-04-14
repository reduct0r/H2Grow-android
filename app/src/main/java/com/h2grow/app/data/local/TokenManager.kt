package com.h2grow.app.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.h2grow.app.api.AuthApiService
import com.h2grow.app.data.remote.RetrofitClient.tokenManager
import com.h2grow.app.domain.model.auth.RefreshRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TokenManager(
    private val authDataStore: DataStore<Preferences>
) {
    private val mutex = Mutex()

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        authDataStore.edit { preferences ->
            preferences[PreferencesKeys.JWT_ACCESS_TOKEN] = accessToken
            preferences[PreferencesKeys.JWT_REFRESH_TOKEN] = refreshToken
        }
    }

    val accessTokenFlow: Flow<String?> = authDataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.JWT_ACCESS_TOKEN]
        }

    val refreshTokenFlow: Flow<String?> = authDataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.JWT_REFRESH_TOKEN]
        }

    suspend fun getAccessToken(): String? = authDataStore.data.first()[PreferencesKeys.JWT_ACCESS_TOKEN]

    suspend fun getRefreshToken(): String? = authDataStore.data.first()[PreferencesKeys.JWT_REFRESH_TOKEN]

    suspend fun clearTokens() {
        authDataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun refreshTokens(authApi: AuthApiService): Boolean = mutex.withLock {
        val currentRefreshToken = getRefreshToken() ?: return false

        try {
            val response = authApi.refreshToken(RefreshRequest(currentRefreshToken))

            if (response.isSuccessful) {
                val newTokens = response.body() ?: return false

                saveTokens(newTokens.accessToken, newTokens.refreshToken)
                return true
            } else {
                return false
            }
        } catch (e: Exception) {
            Log.e("refreshTokens", e.message.orEmpty())
            return false
        }
    }
}