package com.h2grow.app.presentation.main

sealed interface NetworkState {
    data object NoConnection: NetworkState
    data object Loading: NetworkState
}