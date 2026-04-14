package com.h2grow.app.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h2grow.app.data.local.TokenManager
import com.h2grow.app.data.remote.RetrofitClient
import com.h2grow.app.domain.model.auth.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var passwordConfirm by mutableStateOf("")
        private set

    fun onEmailChanged(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        password = newPassword
        validatePassword(newPassword)
        validateConfirmPassword(newPassword, passwordConfirm)
    }

    fun onPasswordConfirmChanged(newConfirm: String) {
        passwordConfirm = newConfirm
        validateConfirmPassword(password, newConfirm)
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
                val request = RegisterRequest(email, password)
                val response = RetrofitClient.mainApiService.register(request)

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