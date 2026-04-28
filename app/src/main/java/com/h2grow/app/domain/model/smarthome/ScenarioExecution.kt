package com.h2grow.app.domain.model.smarthome

data class ScenarioExecution(
    val id: Long,
    val title: String,
    val commands: List<String>,
    val sentAt: String
)
