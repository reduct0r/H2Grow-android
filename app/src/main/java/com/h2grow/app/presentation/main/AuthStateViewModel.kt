package com.h2grow.app.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h2grow.app.data.local.TokenManager
import com.h2grow.app.data.local.TokenManager.RefreshStatus
import com.h2grow.app.data.remote.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthStateViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val retrofitClient: RetrofitClient
) : ViewModel() {

    private val _initialScreenState = MutableStateFlow<InitialScreenState>(InitialScreenState.Loading)
    val initialScreenState: StateFlow<InitialScreenState> = _initialScreenState.asStateFlow()

    init {
        checkInitialScreen()
    }

    private fun checkInitialScreen() {
        viewModelScope.launch {
            _initialScreenState.value = InitialScreenState.Loading

            try {
                val refreshToken = tokenManager.getRefreshToken()
                if (refreshToken.isNullOrBlank()) {
                    _initialScreenState.value = InitialScreenState.Login
                    return@launch
                }

                val refreshStatus = tokenManager.refreshTokens(retrofitClient.refreshAuthApiService)

                _initialScreenState.value = when (refreshStatus) {
                    RefreshStatus.Success -> InitialScreenState.Home
                    RefreshStatus.InvalidToken -> {
                        tokenManager.clearTokens()
                        InitialScreenState.Login
                    }
                    RefreshStatus.NetworkError -> InitialScreenState.NoConnection
                }

            } catch (e: Exception) {
                _initialScreenState.value = InitialScreenState.NoConnection
                Log.e("AuthState", "Failed to check initial screen", e)
            }
        }
    }

    fun retry() {
        checkInitialScreen()
    }
}

