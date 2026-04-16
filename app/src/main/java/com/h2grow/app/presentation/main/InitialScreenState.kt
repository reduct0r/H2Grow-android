package com.h2grow.app.presentation.main

sealed class InitialScreenState {
    data object Loading : InitialScreenState()
    data object Login : InitialScreenState()
    data object Home : InitialScreenState()
    data object NoConnection : InitialScreenState()
}