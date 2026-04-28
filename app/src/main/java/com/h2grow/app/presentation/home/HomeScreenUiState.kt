package com.h2grow.app.presentation.home

import com.h2grow.app.domain.model.smarthome.Home

data class HomeScreenUiState(
    val homes: List<Home> = emptyList(),
    val selectedHome: Home? = null,
    val onboardingStep: OnboardingStep = OnboardingStep.CreateHome,
    val homeNameInput: String = ""
) {
    val canEditHomes: Boolean =
        onboardingStep == OnboardingStep.CreateHome || onboardingStep == OnboardingStep.Completed
}
