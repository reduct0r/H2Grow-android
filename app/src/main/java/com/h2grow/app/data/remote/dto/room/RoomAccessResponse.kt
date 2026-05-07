package com.h2grow.app.data.remote.dto.room

data class RoomAccessResponse(
    val id: Long,
    val userId: Long,
    val userEmail: String,
    val canManage: Boolean
)
