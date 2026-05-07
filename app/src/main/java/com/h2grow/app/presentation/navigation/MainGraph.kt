package com.h2grow.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.h2grow.app.presentation.home.HomeRoute

@Composable
fun MainGraph(backStack: NavBackStack<NavKey>) {
    fun navigate(screen: Screen) {
        if (backStack.lastOrNull() != screen) {
            backStack.add(screen)
        }
    }

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Screen.Home> {
                HomeRoute()
            }
        }
    )
}
