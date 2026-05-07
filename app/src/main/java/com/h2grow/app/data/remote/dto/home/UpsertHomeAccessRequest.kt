package com.h2grow.app.data.remote.dto.home

data class UpsertHomeAccessRequest(
    val userEmail: String,
    val role: HomeRole
)