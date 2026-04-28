package com.h2grow.app.presentation.scenarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h2grow.app.data.local.OnboardingPreferencesRepository
import com.h2grow.app.domain.repository.SmartHomeRepository
import com.h2grow.app.presentation.home.OnboardingStep
import com.h2grow.app.presentation.smarthome.resolveOnboardingStep
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ScenariosViewModel @Inject constructor(
    private val smartHomeRepository: SmartHomeRepository,
    private val onboardingPreferencesRepository: OnboardingPreferencesRepository
) : ViewModel() {
    private val scenarioNameInput = MutableStateFlow("")
    private val scenarioCommandsInput = MutableStateFlow("")

    val uiState: StateFlow<ScenariosUiState> = combine(
        smartHomeRepository.observeSmartHome(),
        onboardingPreferencesRepository.isOnboardingCompletedFlow,
        scenarioNameInput,
        scenarioCommandsInput
    ) { data, isOnboardingCompleted, nameInput, commandsInput ->
        val onboardingStep = resolveOnboardingStep(data, isOnboardingCompleted)
        if (onboardingStep == OnboardingStep.Completed && !isOnboardingCompleted) {
            onboardingPreferencesRepository.setOnboardingCompleted(true)
        }

        ScenariosUiState(
            scenarioExecutions = data.scenarioExecutions,
            onboardingStep = onboardingStep,
            scenarioNameInput = nameInput,
            scenarioCommandsInput = commandsInput
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScenariosUiState(onboardingStep = OnboardingStep.CreateHome)
    )

    fun updateScenarioNameInput(value: String) {
        scenarioNameInput.value = value
    }

    fun updateScenarioCommandsInput(value: String) {
        scenarioCommandsInput.value = value
    }

    fun sendScenario() {
        viewModelScope.launch {
            val commands = uiState.value.scenarioCommandsInput
                .lineSequence()
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .toList()

            if (commands.isEmpty()) return@launch

            val title = uiState.value.scenarioNameInput.ifBlank {
                "Scenario ${uiState.value.scenarioExecutions.size + 1}"
            }

            smartHomeRepository.sendScenario(title, commands)
            scenarioNameInput.value = ""
            scenarioCommandsInput.value = ""
        }
    }
}
