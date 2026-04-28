package com.h2grow.app.data.repository

import com.h2grow.app.domain.model.smarthome.Home
import com.h2grow.app.domain.model.smarthome.MqttCommand
import com.h2grow.app.domain.model.smarthome.Room
import com.h2grow.app.domain.model.smarthome.ScenarioExecution
import com.h2grow.app.domain.model.smarthome.SmartHomeData
import com.h2grow.app.domain.model.smarthome.VirtualDevice
import com.h2grow.app.domain.repository.SmartHomeRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

@Singleton
class InMemorySmartHomeRepository @Inject constructor() : SmartHomeRepository {

    private val storage = MutableStateFlow(SmartHomeStorage())
    private var nextId = 1L

    override fun observeSmartHome(): Flow<SmartHomeData> = storage.map { state ->
        val selectedHome = state.homes.firstOrNull { it.id == state.selectedHomeId }
        val rooms = selectedHome?.let { home ->
            state.rooms.filter { it.homeId == home.id }
        }.orEmpty()
        val selectedRoom = rooms.firstOrNull { it.id == state.selectedRoomId }
        val devices = selectedRoom?.let { room ->
            state.devices.filter { it.roomId == room.id }
        }.orEmpty()
        val selectedDevice = devices.firstOrNull { it.id == state.selectedDeviceId }

        SmartHomeData(
            homes = state.homes,
            selectedHomeId = selectedHome?.id,
            rooms = rooms,
            selectedRoomId = selectedRoom?.id,
            devices = devices,
            selectedDeviceId = selectedDevice?.id,
            scenarioExecutions = state.scenarioExecutions
        )
    }

    override suspend fun createHome(name: String) {
        val home = Home(
            id = nextId(),
            name = name.trim()
        )

        storage.update { current ->
            current.copy(
                homes = current.homes + home,
                selectedHomeId = home.id,
                selectedRoomId = null,
                selectedDeviceId = null
            ).normalize()
        }
    }

    override suspend fun selectHome(homeId: Long) {
        storage.update { current ->
            current.copy(
                selectedHomeId = homeId
            ).normalize()
        }
    }

    override suspend fun createRoom(name: String) {
        val selectedHomeId = storage.value.selectedHomeId ?: return
        val room = Room(
            id = nextId(),
            homeId = selectedHomeId,
            name = name.trim()
        )

        storage.update { current ->
            current.copy(
                rooms = current.rooms + room,
                selectedRoomId = room.id,
                selectedDeviceId = null
            ).normalize()
        }
    }

    override suspend fun selectRoom(roomId: Long) {
        storage.update { current ->
            current.copy(
                selectedRoomId = roomId
            ).normalize()
        }
    }

    override suspend fun createVirtualDevice(name: String) {
        val selectedRoomId = storage.value.selectedRoomId ?: return
        val device = VirtualDevice(
            id = nextId(),
            roomId = selectedRoomId,
            name = name.trim()
        )

        storage.update { current ->
            current.copy(
                devices = current.devices + device,
                selectedDeviceId = device.id
            ).normalize()
        }
    }

    override suspend fun selectDevice(deviceId: Long) {
        storage.update { current ->
            current.copy(
                selectedDeviceId = deviceId
            ).normalize()
        }
    }

    override suspend fun sendCommandToSelectedDevice(command: String) {
        val selectedDeviceId = storage.value.selectedDeviceId ?: return
        val mqttCommand = MqttCommand(
            id = nextId(),
            payload = command.trim(),
            sentAt = nowLabel()
        )

        storage.update { current ->
            current.copy(
                devices = current.devices.map { device ->
                    if (device.id == selectedDeviceId) {
                        device.copy(commands = listOf(mqttCommand) + device.commands)
                    } else {
                        device
                    }
                }
            ).normalize()
        }
    }

    override suspend fun sendScenario(title: String, commands: List<String>) {
        val execution = ScenarioExecution(
            id = nextId(),
            title = title.trim(),
            commands = commands,
            sentAt = nowLabel()
        )

        storage.update { current ->
            current.copy(
                scenarioExecutions = listOf(execution) + current.scenarioExecutions
            ).normalize()
        }
    }

    private fun nextId(): Long = nextId++

    private fun nowLabel(): String = LocalTime.now()
        .format(DateTimeFormatter.ofPattern("HH:mm:ss"))

    private fun SmartHomeStorage.normalize(): SmartHomeStorage {
        val selectedHome = homes.firstOrNull { it.id == selectedHomeId } ?: homes.firstOrNull()
        val resolvedHomeId = selectedHome?.id

        val roomsForSelectedHome = resolvedHomeId?.let { homeId ->
            rooms.filter { it.homeId == homeId }
        }.orEmpty()
        val selectedRoom = roomsForSelectedHome.firstOrNull { it.id == selectedRoomId }
            ?: roomsForSelectedHome.firstOrNull()
        val resolvedRoomId = selectedRoom?.id

        val devicesForSelectedRoom = resolvedRoomId?.let { roomId ->
            devices.filter { it.roomId == roomId }
        }.orEmpty()
        val selectedDevice = devicesForSelectedRoom.firstOrNull { it.id == selectedDeviceId }
            ?: devicesForSelectedRoom.firstOrNull()

        return copy(
            selectedHomeId = resolvedHomeId,
            selectedRoomId = resolvedRoomId,
            selectedDeviceId = selectedDevice?.id
        )
    }

    private data class SmartHomeStorage(
        val homes: List<Home> = emptyList(),
        val rooms: List<Room> = emptyList(),
        val devices: List<VirtualDevice> = emptyList(),
        val scenarioExecutions: List<ScenarioExecution> = emptyList(),
        val selectedHomeId: Long? = null,
        val selectedRoomId: Long? = null,
        val selectedDeviceId: Long? = null
    )
}
