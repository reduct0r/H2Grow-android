package com.h2grow.app.presentation.rooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.h2grow.app.presentation.home.OnboardingStep
import com.h2grow.app.presentation.navigation.Screen
import com.h2grow.app.presentation.smarthome.EmptyState
import com.h2grow.app.presentation.smarthome.OnboardingNavigationEffect
import com.h2grow.app.presentation.smarthome.PreviewFrame
import com.h2grow.app.presentation.smarthome.RoundedSection
import com.h2grow.app.presentation.smarthome.SmartHomeScreen
import com.h2grow.app.presentation.smarthome.previewRoomsUiState

@Composable
fun RoomsRoute(
    onNavigate: (Screen) -> Unit,
    viewModel: RoomsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    OnboardingNavigationEffect(
        currentScreen = Screen.Rooms,
        onboardingStep = uiState.onboardingStep,
        onNavigate = onNavigate
    )

    RoomsScreen(
        uiState = uiState,
        onNavigate = onNavigate,
        onRoomNameChanged = viewModel::updateRoomNameInput,
        onSelectRoom = viewModel::selectRoom,
        onAddRoom = viewModel::createRoom
    )
}

@Composable
fun RoomsScreen(
    uiState: RoomsUiState,
    onNavigate: (Screen) -> Unit,
    onRoomNameChanged: (String) -> Unit,
    onSelectRoom: (Long) -> Unit,
    onAddRoom: () -> Unit
) {
    val currentHomeMessage = when (uiState.onboardingStep) {
        OnboardingStep.CreateHome -> "Create a home first."
        OnboardingStep.AddRooms -> "This is the active onboarding step. Add the first room here."
        OnboardingStep.AddDevices -> "Room step is complete. Device creation is next."
        OnboardingStep.Completed -> "Rooms are ready for normal navigation."
    }

    SmartHomeScreen(
        currentScreen = Screen.Rooms,
        title = "Rooms",
        subtitle = "A minimal room screen to keep the onboarding flow moving.",
        onboardingStep = uiState.onboardingStep,
        onNavigate = onNavigate
    ) {
        RoundedSection(
            title = "Current home",
            subtitle = uiState.selectedHome?.name ?: "Create or select a home first."
        ) {
            if (uiState.selectedHome == null) {
                EmptyState("Rooms are unavailable until a home exists.")
            } else {
                Text(
                    text = currentHomeMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        RoundedSection(
            title = "Rooms",
            subtitle = "Simple placeholder screen."
        ) {
            if (uiState.rooms.isEmpty()) {
                EmptyState("This home has no rooms yet.")
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.rooms, key = { it.id }) { room ->
                        FilterChip(
                            selected = uiState.selectedRoom?.id == room.id,
                            onClick = { onSelectRoom(room.id) },
                            label = { Text(room.name) },
                            enabled = uiState.onboardingStep == OnboardingStep.Completed
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = uiState.roomNameInput,
                    onValueChange = onRoomNameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.canEditRooms && uiState.selectedHome != null,
                    label = { Text("Room name") },
                    placeholder = { Text("Example: Living room") },
                    shape = MaterialTheme.shapes.large
                )

                Button(
                    onClick = onAddRoom,
                    enabled = uiState.canEditRooms && uiState.selectedHome != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add room")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRoomsScreenAddRooms() {
    PreviewFrame {
        RoomsScreen(
            uiState = previewRoomsUiState(step = OnboardingStep.AddRooms),
            onNavigate = {},
            onRoomNameChanged = {},
            onSelectRoom = {},
            onAddRoom = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRoomsScreenCompleted() {
    PreviewFrame {
        RoomsScreen(
            uiState = previewRoomsUiState(step = OnboardingStep.Completed),
            onNavigate = {},
            onRoomNameChanged = {},
            onSelectRoom = {},
            onAddRoom = {}
        )
    }
}
