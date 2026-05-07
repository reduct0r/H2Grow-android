package com.h2grow.app.api

import com.h2grow.app.data.remote.dto.home.CreateHomeRequest
import com.h2grow.app.data.remote.dto.home.HomeAccessResponse
import com.h2grow.app.data.remote.dto.home.HomeResponse
import com.h2grow.app.data.remote.dto.home.UpdateHomeRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface HomeApiService {

    @POST("config/homes")
    suspend fun createHome(@Body request: CreateHomeRequest): HomeResponse

    @GET("config/homes")
    suspend fun getHomes(): List<HomeResponse>

    @GET("config/homes/{homeId}")
    suspend fun getHome(@Path("homeId") homeId: Long): HomeResponse

    @PATCH("config/homes/{homeId}")
    suspend fun updateHome(
        @Path("homeId") homeId: Long,
        @Body request: UpdateHomeRequest
    ): HomeResponse

    @DELETE("config/homes/{homeId}")
    suspend fun deleteHome(@Path("homeId") homeId: Long): HomeResponse

    // ACCESS CONTROL

    @GET("config/homes/{homeId}/access")
    suspend fun getHomeAccessList(@Path("homeId") homeId: Long): HomeAccessResponse

    @PUT("config/homes/{homeId}/access")
    suspend fun grantHomeAccess(
        @Path("homeId") homeId: Long,
        @Body request: CreateHomeRequest
    ): HomeAccessResponse

    @GET("config/homes/{homeId}/access/{userId}")
    suspend fun removeHomeAccess(
        @Path("homeId") homeId: Long,
        @Path("userId") userId: Long
    )
}