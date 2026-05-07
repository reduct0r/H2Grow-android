package com.h2grow.app.api

import com.h2grow.app.data.remote.dto.device.CreateDeviceRequest
import com.h2grow.app.data.remote.dto.device.DeviceCommandRequest
import com.h2grow.app.data.remote.dto.device.DeviceCommandResponse
import com.h2grow.app.data.remote.dto.device.DeviceResponse
import com.h2grow.app.data.remote.dto.device.UpdateDeviceRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface DeviceApiService {

    @POST("config/devices")
    suspend fun createDevice(@Body request: CreateDeviceRequest): DeviceResponse

    @GET("config/rooms/{roomId}/devices")
    suspend fun listDevices(@Path("roomId") roomId: Long): List<DeviceResponse>

    @GET("config/devices/{deviceId}")
    suspend fun getDevice(@Path("deviceId") deviceId: Long): DeviceResponse

    @PATCH("config/devices/{deviceId}")
    suspend fun updateDevice(
        @Path("deviceId") deviceId: Long,
        @Body request: UpdateDeviceRequest
    ): DeviceResponse

    @DELETE("config/devices/{deviceId}")
    suspend fun deleteDevice(@Path("deviceId") deviceId: Long)

    @POST("devices/{deviceId}/commands")
    suspend fun sendDeviceCommand(
        @Path("deviceId") deviceId: Long,
        @Body request: DeviceCommandRequest
    ): DeviceCommandResponse
}
