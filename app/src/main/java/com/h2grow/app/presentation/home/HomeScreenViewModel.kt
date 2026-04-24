package com.h2grow.app.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(): ViewModel() {
    private val _uiState: MutableStateFlow<HomeScreenUiState> = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    fun onboardingHomeStepCompleted() {
        _uiState.update {
            it.copy(
                onboardingStep = OnboardingStep.AddRooms
            )
        }
    }

    fun onboardingRoomsStepCompleted() {
        _uiState.update {
            it.copy(
                onboardingStep = OnboardingStep.AddDevices
            )
        }
    }

    fun onboardingDevicesStepCompleted() {
        _uiState.update {
            it.copy(
                onboardingStep = OnboardingStep.Completed
            )
        }
    }
}