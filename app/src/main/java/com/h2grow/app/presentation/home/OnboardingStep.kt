package com.h2grow.app.presentation.home

sealed interface OnboardingStep {
    data object CreateHome: OnboardingStep
    data object AddRooms: OnboardingStep
    data object AddDevices: OnboardingStep
    data object Completed: OnboardingStep
}