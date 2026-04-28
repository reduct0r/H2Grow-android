package com.h2grow.app.presentation.device

import com.h2grow.app.domain.model.smarthome.Home
import com.h2grow.app.domain.model.smarthome.Room
import com.h2grow.app.domain.model.smarthome.VirtualDevice
import com.h2grow.app.presentation.home.OnboardingStep

data class DeviceUiState(
    val selectedHome: Home? = null,
    val selectedRoom: Room? = null,
    val devices: List<VirtualDevice> = emptyList(),
    val selectedDevice: VirtualDevice? = null,
    val onboardingStep: OnboardingStep = OnboardingStep.CreateHome,
    val deviceNameInput: String = "",
    val deviceCommandInput: String = ""
) {
    val canEditDevices: Boolean =
        onboardingStep == OnboardingStep.AddDevices || onboardingStep == OnboardingStep.Completed
}
