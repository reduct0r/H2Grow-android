package com.h2grow.app.domain.model.auth

data class LoginRequest(
    val email: String,
    val password: String
)
