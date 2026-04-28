package com.h2grow.app.domain.model.smarthome

data class MqttCommand(
    val id: Long,
    val payload: String,
    val sentAt: String
)
