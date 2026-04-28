package com.h2grow.app.domain.model.smarthome

data class VirtualDevice(
    val id: Long,
    val roomId: Long,
    val name: String,
    val type: String = "Заглушка",
    val commands: List<MqttCommand> = emptyList()
)
