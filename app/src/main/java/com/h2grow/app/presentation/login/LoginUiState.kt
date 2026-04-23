package com.h2grow.app.presentation.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val email: String = "",
    val password: String = ""
)
