package com.h2grow.app.data.remote.dto.device

data class DeviceCommandRequest(
    val requestId: String? = null,
    val ts: String? = null,
    val switchable: SwitchableCommandPayload? = null,
    val dimmable: DimmableCommandPayload? = null,
    val sensable: SensableCommandPayload? = null
)

data class SwitchableCommandPayload(
    val isOn: Boolean
)

data class DimmableCommandPayload(
    val value: Int
)

data class SensableCommandPayload(
    val sampleIntervalSec: Int? = null,
    val requestedTypes: List<MeasurementType>? = null
)
