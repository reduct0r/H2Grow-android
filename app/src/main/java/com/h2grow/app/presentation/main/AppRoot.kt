package com.h2grow.app.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.h2grow.app.presentation.navigation.AuthGraph
import com.h2grow.app.presentation.navigation.LoadingScreen
import com.h2grow.app.presentation.navigation.NoConnectionScreen
import com.h2grow.app.presentation.navigation.Screen

@Composable
fun AppRoot() {
    val viewModel: AuthRootViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    when (state) {
        MainUiState.Loading -> {
            LoadingScreen()
        }

        MainUiState.NoConnection -> {
            NoConnectionScreen(
                onRetry = viewModel::retry
            )
        }

        MainUiState.Unauthenticated -> {
            AuthGraph(
                onAuthSuccess = viewModel::onLoginSuccess
            )
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