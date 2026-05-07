package com.h2grow.app.api

import com.h2grow.app.data.remote.dto.room.CreateRoomRequest
import com.h2grow.app.data.remote.dto.room.RoomAccessResponse
import com.h2grow.app.data.remote.dto.room.RoomResponse
import com.h2grow.app.data.remote.dto.room.UpdateRoomRequest
import com.h2grow.app.data.remote.dto.room.UpsertRoomAccessRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RoomApiService {

    @POST("config/rooms")
    suspend fun createRoom(@Body request: CreateRoomRequest): RoomResponse

    @GET("config/homes/{homeId}/rooms")
    suspend fun listRooms(@Path("homeId") homeId: Long): List<RoomResponse>

    @GET("config/rooms/{roomId}")
    suspend fun getRoom(@Path("roomId") roomId: Long): RoomResponse

    @PATCH("config/rooms/{roomId}")
    suspend fun updateRoom(
        @Path("roomId") roomId: Long,
        @Body request: UpdateRoomRequest
    ): RoomResponse

    @DELETE("config/rooms/{roomId}")
    suspend fun deleteRoom(@Path("roomId") roomId: Long)

    // ACCESS CONTROL

    @GET("config/rooms/{roomId}/access")
    suspend fun listRoomAccess(@Path("roomId") roomId: Long): List<RoomAccessResponse>

    @PUT("config/rooms/{roomId}/access")
    suspend fun upsertRoomAccess(
        @Path("roomId") roomId: Long,
        @Body request: UpsertRoomAccessRequest
    ): RoomAccessResponse

    @DELETE("config/rooms/{roomId}/access/{userId}")
    suspend fun removeRoomAccess(
        @Path("roomId") roomId: Long,
        @Path("userId") userId: Long
    )
}
