package com.h2grow.app.data.remote.dto.home

data class HomeAccessResponse(
    val id: Long,
    val userId: Long,
    val userEmail: String,
    val role: HomeRole
)