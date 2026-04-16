package com.h2grow.app.data.remote

import com.h2grow.app.api.AuthApiService
import com.h2grow.app.data.local.TokenManager
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
        if (shouldSkipRefresh(response.request.url.encodedPath)) {
            return null
        }

        if (responseCount(response) >= 3) {
            runBlocking { onRefreshFailed() }
            return null
        }

        val refreshStatus = runBlocking {
            tokenManager.refreshTokens(refreshApi)
        }

        when (refreshStatus) {
            TokenManager.RefreshStatus.Success -> Unit
            TokenManager.RefreshStatus.InvalidToken -> {
                runBlocking { onRefreshFailed() }
                return null
            }
            TokenManager.RefreshStatus.NetworkError -> {
                return null
            }
        }

        val newAccessToken = runBlocking { tokenManager.getAccessToken() } ?: return null

        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()
    }

    private fun shouldSkipRefresh(path: String): Boolean {
        return path.endsWith("/auth/login") ||
            path.endsWith("/auth/register") ||
            path.endsWith("/auth/refresh")
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