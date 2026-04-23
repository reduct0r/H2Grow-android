package com.h2grow.app.presentation.register

data class RegisterUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val password: String = "",
    val passwordConfirm: String = "",
    val email: String = ""
)
