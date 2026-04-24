package com.h2grow.app.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.h2grow.app.presentation.navigation.AuthGraph
import com.h2grow.app.presentation.navigation.Screen

@Composable
fun AppEntry() {
    val authBackStack = rememberNavBackStack(Screen.Login)
    AppRoot(authBackStack = authBackStack)
}

@Composable
fun AppRoot(authBackStack: NavBackStack<NavKey>) {
    val viewModel: AuthRootViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    when (state) {
        MainUiState.Loading -> {
            LoadingScreen()
        }

        MainUiState.Unauthenticated -> {
            AuthGraph(backStack = authBackStack)
        }

        MainUiState.Authenticated -> {
            Text(
                text = "Home",
                modifier = Modifier.fillMaxSize(),
                style = MaterialTheme.typography.headlineMedium
            )
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