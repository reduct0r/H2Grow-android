package com.h2grow.app.presentation.scenarios

import com.h2grow.app.domain.model.smarthome.ScenarioExecution
import com.h2grow.app.presentation.home.OnboardingStep

data class ScenariosUiState(
    val scenarioExecutions: List<ScenarioExecution> = emptyList(),
    val onboardingStep: OnboardingStep = OnboardingStep.CreateHome,
    val scenarioNameInput: String = "",
    val scenarioCommandsInput: String = ""
) {
    val canUseScenarios: Boolean = onboardingStep == OnboardingStep.Completed
}
