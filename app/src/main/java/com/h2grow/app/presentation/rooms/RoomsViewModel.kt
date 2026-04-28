package com.h2grow.app.presentation.rooms

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
class RoomsViewModel @Inject constructor(
    private val smartHomeRepository: SmartHomeRepository,
    private val onboardingPreferencesRepository: OnboardingPreferencesRepository
) : ViewModel() {
    private val roomNameInput = MutableStateFlow("")

    val uiState: StateFlow<RoomsUiState> = combine(
        smartHomeRepository.observeSmartHome(),
        onboardingPreferencesRepository.isOnboardingCompletedFlow,
        roomNameInput
    ) { data, isOnboardingCompleted, input ->
        val onboardingStep = resolveOnboardingStep(data, isOnboardingCompleted)
        if (onboardingStep == OnboardingStep.Completed && !isOnboardingCompleted) {
            onboardingPreferencesRepository.setOnboardingCompleted(true)
        }

        RoomsUiState(
            selectedHome = data.homes.firstOrNull { it.id == data.selectedHomeId },
            rooms = data.rooms,
            selectedRoom = data.rooms.firstOrNull { it.id == data.selectedRoomId },
            onboardingStep = onboardingStep,
            roomNameInput = input
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RoomsUiState(onboardingStep = OnboardingStep.CreateHome)
    )

    fun updateRoomNameInput(value: String) {
        roomNameInput.value = value
    }

    fun selectRoom(roomId: Long) {
        viewModelScope.launch {
            smartHomeRepository.selectRoom(roomId)
        }
    }

    fun createRoom() {
        viewModelScope.launch {
            val name = uiState.value.roomNameInput.ifBlank {
                "Room ${uiState.value.rooms.size + 1}"
            }

            smartHomeRepository.createRoom(name)
            roomNameInput.value = ""
        }
    }
}
