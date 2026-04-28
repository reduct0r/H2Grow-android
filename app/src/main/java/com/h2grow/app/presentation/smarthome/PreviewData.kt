package com.h2grow.app.presentation.smarthome

import androidx.compose.runtime.Composable
import com.h2grow.app.R
import com.h2grow.app.domain.model.components.smartTile.SmartTile
import com.h2grow.app.domain.model.smarthome.Home
import com.h2grow.app.domain.model.smarthome.MqttCommand
import com.h2grow.app.domain.model.smarthome.Room
import com.h2grow.app.domain.model.smarthome.ScenarioExecution
import com.h2grow.app.domain.model.smarthome.VirtualDevice
import com.h2grow.app.presentation.device.DeviceUiState
import com.h2grow.app.presentation.home.HomeScreenUiState
import com.h2grow.app.presentation.home.OnboardingStep
import com.h2grow.app.presentation.rooms.RoomsUiState
import com.h2grow.app.presentation.scenarios.ScenariosUiState
import com.h2grow.app.ui.theme.H2GrowTheme

@Composable
fun PreviewFrame(content: @Composable () -> Unit) {
    H2GrowTheme(dynamicColor = false) {
        content()
    }
}

fun previewHomes(): List<Home> = listOf(
    Home(id = 1L, name = "Green House"),
    Home(id = 2L, name = "Demo Loft")
)

fun previewRooms(): List<Room> = listOf(
    Room(id = 1L, homeId = 1L, name = "Living room"),
    Room(id = 2L, homeId = 1L, name = "Grow room")
)

fun previewCommands(): List<MqttCommand> = listOf(
    MqttCommand(id = 1L, payload = "pump/start", sentAt = "08:00:00"),
    MqttCommand(id = 2L, payload = "light/on", sentAt = "08:02:10")
)

fun previewDevices(): List<VirtualDevice> = listOf(
    VirtualDevice(
        id = 1L,
        roomId = 1L,
        name = "Pump Stub",
        commands = previewCommands()
    ),
    VirtualDevice(
        id = 2L,
        roomId = 1L,
        name = "Light Stub"
    )
)

fun previewScenarios(): List<ScenarioExecution> = listOf(
    ScenarioExecution(
        id = 1L,
        title = "Morning Watering",
        commands = listOf("pump/start", "light/on", "pump/stop"),
        sentAt = "09:15:10"
    )
)

fun previewHomeTiles(): List<SmartTile> = listOf(
    SmartTile.Info(
        id = "temperature",
        title = "Temperature",
        value = "24.3",
        unit = "C",
        icon = R.drawable.ic_launcher_foreground
    ),
    SmartTile.Info(
        id = "humidity",
        title = "Humidity",
        value = "61",
        unit = "%",
        icon = R.drawable.ic_launcher_foreground
    ),
    SmartTile.Toggle(
        id = "pump",
        title = "Pump",
        isOn = false,
        icon = R.drawable.ic_launcher_foreground
    ),
    SmartTile.Dimmer(
        id = "light",
        title = "Light",
        value = 72.0,
        icon = R.drawable.ic_launcher_foreground
    )
)

fun previewHomeUiState(
    step: OnboardingStep = OnboardingStep.Completed
): HomeScreenUiState = HomeScreenUiState(
    homes = previewHomes(),
    selectedHome = previewHomes().first(),
    onboardingStep = step,
    homeNameInput = "Green House"
)

fun previewRoomsUiState(
    step: OnboardingStep = OnboardingStep.AddRooms
): RoomsUiState = RoomsUiState(
    selectedHome = previewHomes().first(),
    rooms = previewRooms(),
    selectedRoom = previewRooms().first(),
    onboardingStep = step,
    roomNameInput = "Grow room"
)

fun previewDeviceUiState(
    step: OnboardingStep = OnboardingStep.AddDevices
): DeviceUiState = DeviceUiState(
    selectedHome = previewHomes().first(),
    selectedRoom = previewRooms().first(),
    devices = previewDevices(),
    selectedDevice = previewDevices().first(),
    onboardingStep = step,
    deviceNameInput = "Pump Stub",
    deviceCommandInput = "pump/start"
)

fun previewScenariosUiState(
    step: OnboardingStep = OnboardingStep.Completed
): ScenariosUiState = ScenariosUiState(
    scenarioExecutions = previewScenarios(),
    onboardingStep = step,
    scenarioNameInput = "Morning Watering",
    scenarioCommandsInput = "pump/start\nlight/on\npump/stop"
)
