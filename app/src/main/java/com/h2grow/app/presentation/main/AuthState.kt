package com.h2grow.app.presentation.main

sealed interface AuthState {
    data object Unauthenticated : AuthState
    data object Authenticated : AuthState
    data object Unknown : AuthState
}