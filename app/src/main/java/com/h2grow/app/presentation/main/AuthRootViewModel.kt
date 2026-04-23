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
class AuthRootViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val retrofitClient: RetrofitClient
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        checkInitialScreen()
    }

    private fun checkInitialScreen() {
        viewModelScope.launch {
            _uiState.value = MainUiState.Loading

            try {
                val refreshToken = tokenManager.getRefreshToken()
                if (refreshToken.isNullOrBlank()) {
                    _uiState.value = MainUiState.Unauthenticated
                    return@launch
                }

                val refreshStatus = tokenManager.refreshTokens(retrofitClient.refreshAuthApiService)

                _uiState.value = when (refreshStatus) {
                    RefreshStatus.Success -> MainUiState.Authenticated

                    RefreshStatus.InvalidToken -> {
                        tokenManager.clearTokens()
                        MainUiState.Unauthenticated
                    }

                    RefreshStatus.NetworkError -> MainUiState.NoConnection
                }

            } catch (e: Exception) {
                _uiState.value = MainUiState.NoConnection
                Log.e("AuthState", "Failed to check initial screen", e)
            }
        }
    }

    fun retry() {
        checkInitialScreen()
    }

    fun onLoginSuccess() {
        _uiState.value = MainUiState.Authenticated
    }
}
