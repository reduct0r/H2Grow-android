package com.h2grow.app.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.h2grow.app.data.local.TokenManager
import com.h2grow.app.data.local.authDataStore
import com.h2grow.app.data.remote.RetrofitClient
import com.h2grow.app.presentation.login.LoginViewModel
import com.h2grow.app.presentation.register.RegisterViewModel

object AuthDependencies {
    @Volatile
    private var tokenManagerInstance: TokenManager? = null

    fun tokenManager(context: Context): TokenManager {
        return tokenManagerInstance ?: synchronized(this) {
            tokenManagerInstance ?: TokenManager(context.applicationContext.authDataStore).also {
                RetrofitClient.initialize(it)
                tokenManagerInstance = it
            }
        }
    }
}

class AuthViewModelFactory(
    private val appContext: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val tokenManager = AuthDependencies.tokenManager(appContext)
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(tokenManager) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(tokenManager) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
