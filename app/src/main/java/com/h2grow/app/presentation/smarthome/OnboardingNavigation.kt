package com.h2grow.app.presentation.smarthome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.h2grow.app.domain.model.smarthome.SmartHomeData
import com.h2grow.app.presentation.home.OnboardingStep
import com.h2grow.app.presentation.navigation.Screen

fun resolveOnboardingStep(
    data: SmartHomeData,
    isOnboardingCompleted: Boolean
): OnboardingStep {
    if (isOnboardingCompleted) return OnboardingStep.Completed
    if (data.homes.isEmpty()) return OnboardingStep.CreateHome
    if (data.rooms.isEmpty()) return OnboardingStep.AddRooms
    if (data.devices.isEmpty()) return OnboardingStep.AddDevices
    return OnboardingStep.Completed
}

fun targetScreenForStep(step: OnboardingStep): Screen? = when (step) {
    OnboardingStep.CreateHome -> Screen.Home
    OnboardingStep.AddRooms -> Screen.Rooms
    OnboardingStep.AddDevices -> Screen.Device
    OnboardingStep.Completed -> null
}

@Composable
fun OnboardingNavigationEffect(
    currentScreen: Screen,
    onboardingStep: OnboardingStep,
    onNavigate: (Screen) -> Unit
) {
    LaunchedEffect(onboardingStep) {
        val target = targetScreenForStep(onboardingStep) ?: return@LaunchedEffect
        if (target != currentScreen) {
            onNavigate(target)
        }
    }
}
