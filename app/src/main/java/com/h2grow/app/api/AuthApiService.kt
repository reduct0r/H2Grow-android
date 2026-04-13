package com.h2grow.app.api

import com.h2grow.app.domain.model.auth.AuthResponse
import com.h2grow.app.domain.model.auth.LoginRequest
import com.h2grow.app.domain.model.auth.RefreshRequest
import com.h2grow.app.domain.model.auth.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body body: RefreshRequest): Response<AuthResponse>
}