package com.h2grow.app.data.remote.dto.device

data class CreateDeviceRequest(
    val roomId: Long,
    val name: String,
    val externalId: String? = null,
    val capabilities: Set<DeviceCapability>,
    val editPolicy: DeviceEditPolicy = DeviceEditPolicy.OWNER_ONLY
)
