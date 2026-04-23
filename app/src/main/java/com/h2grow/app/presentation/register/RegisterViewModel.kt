package com.h2grow.app.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h2grow.app.data.local.TokenManager
import com.h2grow.app.data.remote.RetrofitClient
import com.h2grow.app.domain.model.auth.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val retrofitClient: RetrofitClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
        validatePassword(newPassword)
        validateConfirmPassword(newPassword, _uiState.value.passwordConfirm)
    }

    fun onPasswordConfirmChanged(newConfirm: String) {
        _uiState.update { it.copy(passwordConfirm = newConfirm) }
        validateConfirmPassword(_uiState.value.password, newConfirm)
    }

    fun validatePassword(password: String) {
        val errors = mutableListOf<String>()

        if (password.length < MIN_PASSWORD_LEN) {
            errors.add("The password must contain at least 8 characters")
        }
        if (password.isNotEmpty() && !password.any { it.isLetter() }) {
            errors.add("The password must contain at least one letter")
        }
        if (password.isNotEmpty() && !password.any { it.isDigit() }) {
            errors.add("The password must contain at least one number")
        }
        if (password.isNotEmpty() && !password.any { !it.isLetterOrDigit() }) {
            errors.add("The password must contain at least one special char")
        }

        val errorMessage = if (errors.isEmpty()) null else errors.first()

        _uiState.update { it.copy(passwordError = errorMessage) }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String) {
        val error = if (confirmPassword.isNotEmpty() && password != confirmPassword) {
            "Passwords must be the same"
        } else {
            null
        }
        _uiState.update { it.copy(confirmPasswordError = error) }
    }

    fun register() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val request = RegisterRequest(_uiState.value.email, _uiState.value.password)
                val response = retrofitClient.authApiService.register(request)

                if (response.isSuccessful) {
                    response.body()?.let { authResponse ->
                        tokenManager.saveTokens(authResponse.accessToken, authResponse.refreshToken)
                        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Account already exist") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Server connection error") }
            }
        }
    }

    companion object {
        const val MIN_PASSWORD_LEN = 8
    }
}