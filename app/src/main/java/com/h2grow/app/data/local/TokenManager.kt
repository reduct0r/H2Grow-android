package com.h2grow.app.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.h2grow.app.api.AuthApiService
import com.h2grow.app.data.remote.dto.auth.RefreshRequest
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class TokenManager @Inject constructor(
    private val authDataStore: DataStore<Preferences>,
    private val encryptor: TokenEncryptor
) {
    sealed class RefreshStatus {
        data object Success : RefreshStatus()
        data object InvalidToken : RefreshStatus()
        data object NetworkError : RefreshStatus()
    }

    private val mutex = Mutex()

    @Volatile
    private var accessTokenCache: String? = null

    val isLoggedInFlow: Flow<Boolean> = authDataStore.data
        .map { preferences ->
            !preferences[PreferencesKeys.JWT_ACCESS_TOKEN].isNullOrBlank()
        }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        authDataStore.edit { preferences ->
            val encryptedRefreshToken = encryptor.encrypt(refreshToken)

            preferences[PreferencesKeys.JWT_ACCESS_TOKEN] = accessToken
            preferences[PreferencesKeys.JWT_REFRESH_TOKEN] = encryptedRefreshToken
        }
        accessTokenCache = accessToken
    }

    suspend fun getAccessToken(): String? =
        withContext(Dispatchers.IO) {
            if (accessTokenCache != null) return@withContext accessTokenCache

            val token =
                authDataStore.data.first()[PreferencesKeys.JWT_ACCESS_TOKEN]

            accessTokenCache = token
            token
        }

    suspend fun getRefreshToken(): String? =
        withContext(Dispatchers.IO) {
            val encrypted =
                authDataStore.data.first()[PreferencesKeys.JWT_REFRESH_TOKEN]
                    ?: return@withContext null

            try {
                encryptor.decrypt(encrypted)
            } catch (e: SecurityException) {
                Log.w("TokenManager", "decrypt failed", e)
                clearTokens()
                null
            }
        }

    suspend fun clearTokens() {
        authDataStore.edit { preferences ->
            preferences.clear()
        }
        accessTokenCache = null
    }

    suspend fun refreshTokens(authApi: AuthApiService): RefreshStatus =
        withContext(Dispatchers.IO) {

            val currentRefreshToken = getRefreshToken()
                ?: return@withContext RefreshStatus.InvalidToken

            mutex.withLock {
                try {
                    val response = authApi.refreshToken(
                        RefreshRequest(currentRefreshToken)
                    )

                    if (response.isSuccessful) {
                        val newTokens = response.body()
                            ?: return@withContext RefreshStatus.InvalidToken

                        saveTokens(newTokens.accessToken, newTokens.refreshToken)
                        RefreshStatus.Success
                    } else {
                        if (response.code() == 401 || response.code() == 403) {
                            RefreshStatus.InvalidToken
                        } else {
                            RefreshStatus.NetworkError
                        }
                    }

                } catch (e: Exception) {
                    Log.e("TokenManager", "refresh failed", e)
                    RefreshStatus.NetworkError
                }
            }
        }
}