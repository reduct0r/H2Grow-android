package com.h2grow.app.presentation.main

sealed interface MainUiState {
    data object Loading : MainUiState
    data object NoConnection : MainUiState
    data object Unauthenticated : MainUiState
    data object Authenticated : MainUiState
}