package com.h2grow.app.domain.model.auth

data class LogoutRequest(
    val refreshToken: String
)