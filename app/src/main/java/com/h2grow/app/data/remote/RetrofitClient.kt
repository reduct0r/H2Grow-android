package com.h2grow.app.data.remote

import com.h2grow.app.api.AuthApiService
import com.h2grow.app.data.local.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    lateinit var tokenManager: TokenManager

    // ====================== REFRESH CLIENT ======================
    private val refreshOkHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val refreshRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(refreshOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val refreshAuthApiService: AuthApiService by lazy {
        refreshRetrofit.create(AuthApiService::class.java)
    }

    // ====================== MAIN CLIENT ======================

    private val mainOkHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(tokenManager))
            .authenticator(TokenAuthenticator(
                tokenManager = tokenManager,
                refreshApi = refreshAuthApiService,
                onRefreshFailed = { tokenManager.clearTokens() }
            ))
            .build()
    }

    private val mainRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(mainOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val mainApiService: AuthApiService by lazy {
        mainRetrofit.create(AuthApiService::class.java)
    }

    fun initialize(tokenManager: TokenManager) {
        this.tokenManager = tokenManager
    }
}