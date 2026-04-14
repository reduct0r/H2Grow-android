package com.h2grow.app.data.remote

import android.util.Log
import com.h2grow.app.api.AuthApiService
import com.h2grow.app.data.local.TokenManager
import com.h2grow.app.domain.model.auth.LogoutRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenManager: TokenManager,
    private val refreshApi: AuthApiService,
    private val onRefreshFailed: suspend () -> Unit
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 3) {
            runBlocking { onRefreshFailed() }
            return null
        }

        val refreshSucceeded = runBlocking {
            tokenManager.refreshTokens(refreshApi)
        }

        if (!refreshSucceeded) {
            runBlocking { onRefreshFailed() }
            return null
        }

        val newAccessToken = runBlocking { tokenManager.getAccessToken() } ?: return null

        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var currentResponse = response.priorResponse
        while (currentResponse != null) {
            count++
            currentResponse = currentResponse.priorResponse
        }
        return count
    }
}