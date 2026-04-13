package com.h2grow.app.domain.model.auth

data class RegisterRequest(
    val email: String,
    val password: String
)
