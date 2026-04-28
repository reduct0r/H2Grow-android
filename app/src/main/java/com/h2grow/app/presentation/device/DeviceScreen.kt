package com.h2grow.app.presentation.device

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.h2grow.app.presentation.smarthome.previewDeviceUiState

@Composable
fun DeviceRoute(
    onNavigate: (Screen) -> Unit,
    viewModel: DeviceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    OnboardingNavigationEffect(
        currentScreen = Screen.Device,
        onboardingStep = uiState.onboardingStep,
        onNavigate = onNavigate
    )

    DeviceScreen(
        uiState = uiState,
        onNavigate = onNavigate,
        onDeviceNameChanged = viewModel::updateDeviceNameInput,
        onSelectDevice = viewModel::selectDevice,
        onAddDevice = viewModel::createVirtualDevice,
        onCommandChanged = viewModel::updateCommandInput,
        onSendCommand = viewModel::sendCommandToSelectedDevice
    )
}

@Composable
fun DeviceScreen(
    uiState: DeviceUiState,
    onNavigate: (Screen) -> Unit,
    onDeviceNameChanged: (String) -> Unit,
    onSelectDevice: (Long) -> Unit,
    onAddDevice: () -> Unit,
    onCommandChanged: (String) -> Unit,
    onSendCommand: () -> Unit
) {
    val contextMessage = when (uiState.onboardingStep) {
        OnboardingStep.CreateHome -> "Create a home first."
        OnboardingStep.AddRooms -> "Create a room first."
        OnboardingStep.AddDevices -> "This is the active onboarding step. Add the first virtual device here."
        OnboardingStep.Completed -> "Create the first virtual device, then send MQTT commands to the server."
    }

    SmartHomeScreen(
        currentScreen = Screen.Device,
        title = "Device",
        subtitle = "Minimal placeholder screen for the virtual device stub.",
        onboardingStep = uiState.onboardingStep,
        onNavigate = onNavigate
    ) {
        RoundedSection(
            title = "Context",
            subtitle = buildString {
                append(uiState.selectedHome?.name ?: "Home is not selected")
                append(" / ")
                append(uiState.selectedRoom?.name ?: "Room is not selected")
            }
        ) {
            Text(
                text = contextMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        RoundedSection(
            title = "Virtual devices",
            subtitle = "This is an in-memory stub that can later be replaced with a real API."
        ) {
            if (uiState.devices.isEmpty()) {
                EmptyState("There are no devices in the selected room yet.")
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.devices, key = { it.id }) { device ->
                        FilterChip(
                            selected = uiState.selectedDevice?.id == device.id,
                            onClick = { onSelectDevice(device.id) },
                            label = { Text(device.name) },
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
                    value = uiState.deviceNameInput,
                    onValueChange = onDeviceNameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.canEditDevices && uiState.selectedRoom != null,
                    label = { Text("Device name") },
                    placeholder = { Text("Example: Pump Stub") },
                    shape = MaterialTheme.shapes.large
                )

                Button(
                    onClick = onAddDevice,
                    enabled = uiState.canEditDevices && uiState.selectedRoom != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add virtual device")
                }
            }
        }

        RoundedSection(
            title = "MQTT commands",
            subtitle = "Commands are entered manually by the user and are not hardcoded into the device."
        ) {
            OutlinedTextField(
                value = uiState.deviceCommandInput,
                onValueChange = onCommandChanged,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.canEditDevices && uiState.selectedDevice != null,
                label = { Text("MQTT command") },
                placeholder = { Text("pump/start") },
                shape = MaterialTheme.shapes.large
            )

            Button(
                onClick = onSendCommand,
                enabled = uiState.canEditDevices &&
                    uiState.selectedDevice != null &&
                    uiState.deviceCommandInput.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send to server")
            }

            if (uiState.selectedDevice?.commands.isNullOrEmpty()) {
                EmptyState("Command history is empty.")
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    uiState.selectedDevice?.commands.orEmpty().forEach { command ->
                        RoundedSection(
                            title = command.payload,
                            subtitle = "Sent at ${command.sentAt}",
                            contentPadding = PaddingValues(14.dp)
                        ) {
                            Text(
                                text = "Stored inside the in-memory repository.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDeviceScreenAddDevices() {
    PreviewFrame {
        DeviceScreen(
            uiState = previewDeviceUiState(step = OnboardingStep.AddDevices),
            onNavigate = {},
            onDeviceNameChanged = {},
            onSelectDevice = {},
            onAddDevice = {},
            onCommandChanged = {},
            onSendCommand = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDeviceScreenCompleted() {
    PreviewFrame {
        DeviceScreen(
            uiState = previewDeviceUiState(step = OnboardingStep.Completed),
            onNavigate = {},
            onDeviceNameChanged = {},
            onSelectDevice = {},
            onAddDevice = {},
            onCommandChanged = {},
            onSendCommand = {}
        )
    }
}
