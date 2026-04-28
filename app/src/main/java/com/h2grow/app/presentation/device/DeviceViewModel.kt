package com.h2grow.app.presentation.device

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
class DeviceViewModel @Inject constructor(
    private val smartHomeRepository: SmartHomeRepository,
    private val onboardingPreferencesRepository: OnboardingPreferencesRepository
) : ViewModel() {
    private val deviceNameInput = MutableStateFlow("")
    private val deviceCommandInput = MutableStateFlow("")

    val uiState: StateFlow<DeviceUiState> = combine(
        smartHomeRepository.observeSmartHome(),
        onboardingPreferencesRepository.isOnboardingCompletedFlow,
        deviceNameInput,
        deviceCommandInput
    ) { data, isOnboardingCompleted, nameInput, commandInput ->
        val onboardingStep = resolveOnboardingStep(data, isOnboardingCompleted)
        if (onboardingStep == OnboardingStep.Completed && !isOnboardingCompleted) {
            onboardingPreferencesRepository.setOnboardingCompleted(true)
        }

        DeviceUiState(
            selectedHome = data.homes.firstOrNull { it.id == data.selectedHomeId },
            selectedRoom = data.rooms.firstOrNull { it.id == data.selectedRoomId },
            devices = data.devices,
            selectedDevice = data.devices.firstOrNull { it.id == data.selectedDeviceId },
            onboardingStep = onboardingStep,
            deviceNameInput = nameInput,
            deviceCommandInput = commandInput
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DeviceUiState(onboardingStep = OnboardingStep.CreateHome)
    )

    fun updateDeviceNameInput(value: String) {
        deviceNameInput.value = value
    }

    fun updateCommandInput(value: String) {
        deviceCommandInput.value = value
    }

    fun selectDevice(deviceId: Long) {
        viewModelScope.launch {
            smartHomeRepository.selectDevice(deviceId)
        }
    }

    fun createVirtualDevice() {
        viewModelScope.launch {
            val name = uiState.value.deviceNameInput.ifBlank {
                "Virtual device ${uiState.value.devices.size + 1}"
            }

            smartHomeRepository.createVirtualDevice(name)
            deviceNameInput.value = ""
        }
    }

    fun sendCommandToSelectedDevice() {
        viewModelScope.launch {
            val command = uiState.value.deviceCommandInput.trim()
            if (command.isBlank()) return@launch

            smartHomeRepository.sendCommandToSelectedDevice(command)
            deviceCommandInput.value = ""
        }
    }
}
