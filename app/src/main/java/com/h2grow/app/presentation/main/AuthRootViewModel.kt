package com.h2grow.app.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h2grow.app.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class AuthRootViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = tokenManager.isLoggedInFlow
        .map { isLoggedIn ->
            if (isLoggedIn) MainUiState.Authenticated else MainUiState.Unauthenticated
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainUiState.Loading
        )
}
