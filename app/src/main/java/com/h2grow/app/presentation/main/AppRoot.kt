package com.h2grow.app.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.h2grow.app.presentation.navigation.AuthGraph
import com.h2grow.app.presentation.navigation.MainGraph
import com.h2grow.app.presentation.navigation.Screen

@Composable
fun AppEntry() {
    val authBackStack = rememberNavBackStack(Screen.Login)
    val mainBackStack = rememberNavBackStack(Screen.Home)
    AppRoot(
        authBackStack = authBackStack,
        mainBackStack = mainBackStack
    )
}

@Composable
fun AppRoot(
    authBackStack: NavBackStack<NavKey>,
    mainBackStack: NavBackStack<NavKey>
) {
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
            MainGraph(backStack = mainBackStack)
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

@Preview(showBackground = true)
@Composable
private fun PreviewLoadingScreen() {
        LoadingScreen()
}
