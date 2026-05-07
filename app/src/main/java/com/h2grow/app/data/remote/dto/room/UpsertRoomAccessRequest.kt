package com.h2grow.app.data.remote.dto.room

data class UpsertRoomAccessRequest(
    val userEmail: String,
    val canManage: Boolean
)
