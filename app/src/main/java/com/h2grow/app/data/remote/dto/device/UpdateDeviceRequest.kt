package com.h2grow.app.data.remote.dto.device

data class UpdateDeviceRequest(
    val roomId: Long? = null,
    val name: String? = null,
    val externalId: String? = null,
    val capabilities: Set<DeviceCapability>? = null,
    val editPolicy: DeviceEditPolicy? = null
)
