package com.h2grow.app.presentation.scenarios

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
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
import com.h2grow.app.presentation.navigation.Screen
import com.h2grow.app.presentation.smarthome.EmptyState
import com.h2grow.app.presentation.smarthome.OnboardingNavigationEffect
import com.h2grow.app.presentation.smarthome.PreviewFrame
import com.h2grow.app.presentation.smarthome.RoundedSection
import com.h2grow.app.presentation.smarthome.SmartHomeScreen
import com.h2grow.app.presentation.smarthome.previewScenariosUiState

@Composable
fun ScenariosRoute(
    onNavigate: (Screen) -> Unit,
    viewModel: ScenariosViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    OnboardingNavigationEffect(
        currentScreen = Screen.Scenarios,
        onboardingStep = uiState.onboardingStep,
        onNavigate = onNavigate
    )

    ScenariosScreen(
        uiState = uiState,
        onNavigate = onNavigate,
        onScenarioNameChanged = viewModel::updateScenarioNameInput,
        onScenarioCommandsChanged = viewModel::updateScenarioCommandsInput,
        onSendScenario = viewModel::sendScenario
    )
}

@Composable
fun ScenariosScreen(
    uiState: ScenariosUiState,
    onNavigate: (Screen) -> Unit,
    onScenarioNameChanged: (String) -> Unit,
    onScenarioCommandsChanged: (String) -> Unit,
    onSendScenario: () -> Unit
) {
    SmartHomeScreen(
        currentScreen = Screen.Scenarios,
        title = "Scenarios",
        subtitle = "A simple screen for entering MQTT command sequences.",
        onboardingStep = uiState.onboardingStep,
        onNavigate = onNavigate
    ) {
        RoundedSection(
            title = "New scenario",
            subtitle = "Enter one MQTT command per line."
        ) {
            OutlinedTextField(
                value = uiState.scenarioNameInput,
                onValueChange = onScenarioNameChanged,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.canUseScenarios,
                label = { Text("Scenario name") },
                placeholder = { Text("Example: Morning Watering") },
                shape = MaterialTheme.shapes.large
            )

            OutlinedTextField(
                value = uiState.scenarioCommandsInput,
                onValueChange = onScenarioCommandsChanged,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.canUseScenarios,
                label = { Text("MQTT commands") },
                placeholder = { Text("pump/start\nlight/on\npump/stop") },
                minLines = 5,
                shape = MaterialTheme.shapes.large
            )

            Button(
                onClick = onSendScenario,
                enabled = uiState.canUseScenarios && uiState.scenarioCommandsInput.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send scenario")
            }
        }

        RoundedSection(
            title = "Sent scenarios",
            subtitle = "History is stored in the in-memory repository."
        ) {
            if (uiState.scenarioExecutions.isEmpty()) {
                EmptyState("No scenarios sent yet.")
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    uiState.scenarioExecutions.forEach { execution ->
                        RoundedSection(
                            title = execution.title,
                            subtitle = "Sent at ${execution.sentAt}",
                            contentPadding = PaddingValues(14.dp)
                        ) {
                            Text(
                                text = execution.commands.joinToString(separator = "\n"),
                                style = MaterialTheme.typography.bodyMedium
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
private fun PreviewScenariosScreen() {
    PreviewFrame {
        ScenariosScreen(
            uiState = previewScenariosUiState(),
            onNavigate = {},
            onScenarioNameChanged = {},
            onScenarioCommandsChanged = {},
            onSendScenario = {}
        )
    }
}
