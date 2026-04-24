package com.h2grow.app.presentation.home

data class HomeScreenUiState(
    val onboardingStep: OnboardingStep = OnboardingStep.CreateHome,
    val isLoading: Boolean = false,
)