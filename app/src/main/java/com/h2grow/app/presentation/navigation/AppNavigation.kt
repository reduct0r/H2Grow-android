package com.h2grow.app.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.h2grow.app.presentation.login.LoginRoute
import com.h2grow.app.presentation.login.LoginViewModel
import com.h2grow.app.presentation.main.AuthRootViewModel
import com.h2grow.app.presentation.main.AuthState
import com.h2grow.app.presentation.register.RegisterRoute

//@Composable
//fun AppNavigation() {
//    val authRootViewModel: AuthRootViewModel = hiltViewModel()
//
//    val screenState by authRootViewModel.authState.collectAsState()
//    val backStack = rememberNavBackStack(Screen.Initial)
//
//    LaunchedEffect(screenState) {
//        when (screenState) {
//            is AuthState.Login -> {
//                backStack.clear()
//                backStack.add(Screen.Login)
//            }
//            is AuthState.Home -> {
//                backStack.clear()
//                backStack.add(Screen.Home)
//            }
//            else -> {}
//        }
//
//    }
//
//    when (screenState) {
//        is AuthState.Loading -> {
//            LoadingScreen()
//        }
//
//        is AuthState.NoConnection -> {
//            NoConnectionScreen(
//                onRetry = { authRootViewModel.retry() }
//            )
//        }
//
//        else -> {
//            NavDisplay(
//                backStack = backStack,
//                onBack = { backStack.removeLastOrNull() },
//                entryProvider = entryProvider {
//                    entry<Screen.Login> {
//                        val loginViewModel: LoginViewModel = hiltViewModel()
//                        LoginRoute(
//                            viewModel = loginViewModel,
//                            onLoginSuccess = {
//                                backStack.clear()
//                                backStack.add(Screen.Home)
//                            },
//                            onNavigateToRegister = {
//                                backStack.add(Screen.Register)
//                            }
//                        )
//                    }
//
//                    entry<Screen.Register> {
//                        RegisterRoute(
//                            onRegisterSuccess = {
//                                backStack.clear()
//                                backStack.add(Screen.Home)
//                            },
//                            onNavigateToLogin = {
//                                backStack.removeLastOrNull()
//                            }
//                        )
//                    }
//
//                    entry<Screen.Home> {
//                        Text(
//                            text = "Home",
//                            modifier = Modifier.fillMaxSize(),
//                            style = MaterialTheme.typography.headlineMedium
//                        )
//                    }
//
//                    entry<Screen.Initial> {
//                        LoadingScreen()
//                    }
//                }
//            )
//        }
//    }
//}

@Composable
fun NoConnectionScreen(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No connection to the Internet")
            Spacer(Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
