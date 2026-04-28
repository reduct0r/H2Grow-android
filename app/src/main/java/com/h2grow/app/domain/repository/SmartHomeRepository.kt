package com.h2grow.app.domain.repository

import com.h2grow.app.domain.model.smarthome.SmartHomeData
import kotlinx.coroutines.flow.Flow

interface SmartHomeRepository {
    fun observeSmartHome(): Flow<SmartHomeData>

    suspend fun createHome(name: String)
    suspend fun selectHome(homeId: Long)

    suspend fun createRoom(name: String)
    suspend fun selectRoom(roomId: Long)

    suspend fun createVirtualDevice(name: String)
    suspend fun selectDevice(deviceId: Long)
    suspend fun sendCommandToSelectedDevice(command: String)

    suspend fun sendScenario(title: String, commands: List<String>)
}
