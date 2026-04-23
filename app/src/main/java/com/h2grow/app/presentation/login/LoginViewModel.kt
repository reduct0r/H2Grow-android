package com.h2grow.app.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h2grow.app.data.local.TokenManager
import com.h2grow.app.data.remote.RetrofitClient
import com.h2grow.app.domain.model.auth.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val retrofitClient: RetrofitClient
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        _uiState.update {
            it.copy(
                email = newEmail,
                errorMessage = null
            )
        }
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.update {
            it.copy(
                password = newPassword,
                errorMessage = null
            )
        }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }
            try {
                val request = LoginRequest(_uiState.value.email.trim(), _uiState.value.password)
                val response = retrofitClient.authApiService.login(request)

                if (response.isSuccessful) {
                    response.body()?.let { authResponse ->
                        tokenManager.saveTokens(
                            authResponse.accessToken,
                            authResponse.refreshToken
                        )

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = true
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Incorrect login or password"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Server connection error"
                    )
                }
            }
        }
    }

}