package com.h2grow.app.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.h2grow.app.presentation.login.LoginScreen
import com.h2grow.app.presentation.register.RegisterScreen

@Composable
fun AppNavigation() {
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(Screen.Login)

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