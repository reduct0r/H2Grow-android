package com.h2grow.app.presentation.rooms

import com.h2grow.app.domain.model.smarthome.Home
import com.h2grow.app.domain.model.smarthome.Room
import com.h2grow.app.presentation.home.OnboardingStep

data class RoomsUiState(
    val selectedHome: Home? = null,
    val rooms: List<Room> = emptyList(),
    val selectedRoom: Room? = null,
    val onboardingStep: OnboardingStep = OnboardingStep.CreateHome,
    val roomNameInput: String = ""
) {
    val canEditRooms: Boolean =
        onboardingStep == OnboardingStep.AddRooms || onboardingStep == OnboardingStep.Completed
}
