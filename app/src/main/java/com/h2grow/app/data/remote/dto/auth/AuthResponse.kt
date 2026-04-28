package com.h2grow.app.data.remote.dto.auth

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)
