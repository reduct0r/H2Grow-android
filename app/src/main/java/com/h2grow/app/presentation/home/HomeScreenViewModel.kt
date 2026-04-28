package com.h2grow.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h2grow.app.data.local.OnboardingPreferencesRepository
import com.h2grow.app.domain.repository.SmartHomeRepository
import com.h2grow.app.presentation.smarthome.resolveOnboardingStep
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val smartHomeRepository: SmartHomeRepository,
    private val onboardingPreferencesRepository: OnboardingPreferencesRepository
) : ViewModel() {
    private val homeNameInput = MutableStateFlow("")

    val uiState: StateFlow<HomeScreenUiState> = combine(
        smartHomeRepository.observeSmartHome(),
        onboardingPreferencesRepository.isOnboardingCompletedFlow,
        homeNameInput
    ) { data, isOnboardingCompleted, input ->
        val onboardingStep = resolveOnboardingStep(data, isOnboardingCompleted)
        if (onboardingStep == OnboardingStep.Completed && !isOnboardingCompleted) {
            onboardingPreferencesRepository.setOnboardingCompleted(true)
        }

        HomeScreenUiState(
            homes = data.homes,
            selectedHome = data.homes.firstOrNull { it.id == data.selectedHomeId },
            onboardingStep = onboardingStep,
            homeNameInput = input
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeScreenUiState()
    )

    fun updateHomeNameInput(value: String) {
        homeNameInput.value = value
    }

    fun selectHome(homeId: Long) {
        viewModelScope.launch {
            smartHomeRepository.selectHome(homeId)
        }
    }

    fun createHome() {
        viewModelScope.launch {
            val name = uiState.value.homeNameInput.ifBlank {
                "Home ${uiState.value.homes.size + 1}"
            }

            smartHomeRepository.createHome(name)
            homeNameInput.update { "" }
        }
    }
}
