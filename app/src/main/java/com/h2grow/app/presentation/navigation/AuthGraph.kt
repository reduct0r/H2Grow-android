package com.h2grow.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.h2grow.app.presentation.login.LoginRoute
import com.h2grow.app.presentation.register.RegisterRoute

@Composable
fun AuthGraph(backStack: NavBackStack<NavKey>) {
    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {

            entry<Screen.Login> {
                LoginRoute(
                    onNavigateToRegister = {
                        backStack.add(Screen.Register)
                    }
                )
            }

            entry<Screen.Register> {
                RegisterRoute(
                    onNavigateToLogin = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}