package com.h2grow.app.presentation.home

import com.h2grow.app.presentation.components.DropdownItem

data class HomeScreenUiState(
    val onboardingStep: OnboardingStep = OnboardingStep.CreateHome,
    val isLoading: Boolean = false,
    val selectedHome: DropdownItem? = null,
    val homesList: List<DropdownItem>? = null
)