package com.h2grow.app.data.remote.dto.device

data class DeviceResponse(
    val id: Long,
    val name: String,
    val externalId: String?,
    val roomId: Long,
    val capabilities: Set<DeviceCapability>,
    val canSwitch: Boolean,
    val canDim: Boolean,
    val canSense: Boolean,
    val canControl: Boolean,
    val editPolicy: DeviceEditPolicy
)
