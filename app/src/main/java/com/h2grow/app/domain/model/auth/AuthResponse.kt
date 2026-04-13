package com.h2grow.app.domain.model.auth

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)
