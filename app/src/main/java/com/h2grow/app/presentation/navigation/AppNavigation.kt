package com.h2grow.app.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.h2grow.app.presentation.login.LoginScreen
import com.h2grow.app.presentation.register.RegisterScreen
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.h2grow.app.data.local.TokenManager
import com.h2grow.app.presentation.login.LoginViewModel
import com.h2grow.app.presentation.main.AuthStateViewModel
import com.h2grow.app.presentation.main.InitialScreenState
import com.h2grow.app.presentation.register.RegisterViewModel

@Composable
fun AppNavigation() {
    val authStateViewModel: AuthStateViewModel = hiltViewModel()

    val screenState by authStateViewModel.initialScreenState.collectAsState()

    when (screenState) {

        is InitialScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is InitialScreenState.NoConnection -> {
            NoConnectionScreen(
                onRetry = { authStateViewModel.retry() }
            )
        }

        is InitialScreenState.Login,
        is InitialScreenState.Home -> {

            val startScreen = when (screenState) {
                is InitialScreenState.Login -> Screen.Login
                is InitialScreenState.Home -> Screen.Home
                else -> Screen.Login
            }
            val backStack = rememberNavBackStack(startScreen)

            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = entryProvider {
                    entry<Screen.Login> {
                        val loginViewModel: LoginViewModel = hiltViewModel()
                        LoginScreen(
                            viewModel = loginViewModel,
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
                        val registerViewModel: RegisterViewModel = hiltViewModel()
                        RegisterScreen(
                            viewModel = registerViewModel,
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
                            text = "Home",
                            modifier = Modifier.fillMaxSize(),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            )
        }
    }
}

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
