package com.h2grow.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.h2grow.app.presentation.login.LoginRoute
import com.h2grow.app.presentation.login.LoginViewModel
import com.h2grow.app.presentation.register.RegisterRoute

@Composable
fun AuthGraph(
    onAuthSuccess: () -> Unit
) {
    val backStack = rememberNavBackStack(Screen.Login)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {

            entry<Screen.Login> {
                LoginRoute(
                    onLoginSuccess = onAuthSuccess,
                    onNavigateToRegister = {
                        backStack.add(Screen.Register)
                    }
                )
            }

            entry<Screen.Register> {
                RegisterRoute(
                    onRegisterSuccess = onAuthSuccess,
                    onNavigateToLogin = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}