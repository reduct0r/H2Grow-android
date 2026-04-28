package com.h2grow.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.h2grow.app.presentation.device.DeviceRoute
import com.h2grow.app.presentation.home.HomeRoute
import com.h2grow.app.presentation.rooms.RoomsRoute
import com.h2grow.app.presentation.scenarios.ScenariosRoute

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
                HomeRoute(onNavigate = ::navigate)
            }

            entry<Screen.Rooms> {
                RoomsRoute(onNavigate = ::navigate)
            }

            entry<Screen.Device> {
                DeviceRoute(onNavigate = ::navigate)
            }

            entry<Screen.Scenarios> {
                ScenariosRoute(onNavigate = ::navigate)
            }
        }
    )
}
