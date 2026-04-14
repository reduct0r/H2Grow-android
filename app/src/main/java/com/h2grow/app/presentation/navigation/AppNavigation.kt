package com.h2grow.app.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.h2grow.app.data.remote.RetrofitClient
import com.h2grow.app.presentation.auth.AuthDependencies
import com.h2grow.app.presentation.login.LoginScreen
import com.h2grow.app.presentation.register.RegisterScreen
import androidx.compose.ui.platform.LocalContext

@Composable
fun AppNavigation() {
    val appContext = LocalContext.current.applicationContext
    val tokenManager = remember { AuthDependencies.tokenManager(appContext) }

    var initialScreen by remember { mutableStateOf<Screen?>(null) }

    LaunchedEffect(Unit) {
        val hasRefreshToken = !tokenManager.getRefreshToken().isNullOrBlank()

        initialScreen = if (!hasRefreshToken) {
            Screen.Login
        } else {
            val refreshed = tokenManager.refreshTokens(RetrofitClient.refreshAuthApiService)
            if (refreshed) {
                Screen.Home
            } else {
                tokenManager.clearTokens()
                Screen.Login
            }
        }
    }

    if (initialScreen == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val backStack: NavBackStack<NavKey> = rememberNavBackStack(initialScreen!!)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Screen.Login> {
                LoginScreen(
                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(Screen.Home)
                    },
                    onNavigateToRegister = {
                        backStack.add(Screen.Register)
                    }
                )
            }

            entry<Screen.Register> {
                RegisterScreen(
                    onRegisterSuccess = {
                        backStack.clear()
                        backStack.add(Screen.Home)
                    },
                    onNavigateToLogin = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            entry<Screen.Home> {
                Text(
                    text = "Главная страница",
                    modifier = Modifier.fillMaxSize(),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    )
}