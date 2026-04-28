package com.h2grow.app.domain.model.smarthome

data class SmartHomeData(
    val homes: List<Home> = emptyList(),
    val selectedHomeId: Long? = null,
    val rooms: List<Room> = emptyList(),
    val selectedRoomId: Long? = null,
    val devices: List<VirtualDevice> = emptyList(),
    val selectedDeviceId: Long? = null,
    val scenarioExecutions: List<ScenarioExecution> = emptyList()
)
