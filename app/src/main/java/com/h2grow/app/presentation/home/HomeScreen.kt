package com.h2grow.app.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.h2grow.app.presentation.components.SmartTilesGrid
import com.h2grow.app.presentation.navigation.Screen
import com.h2grow.app.presentation.smarthome.EmptyState
import com.h2grow.app.presentation.smarthome.OnboardingNavigationEffect
import com.h2grow.app.presentation.smarthome.PreviewFrame
import com.h2grow.app.presentation.smarthome.RoundedSection
import com.h2grow.app.presentation.smarthome.SmartHomeScreen
import com.h2grow.app.presentation.smarthome.previewHomeTiles
import com.h2grow.app.presentation.smarthome.previewHomeUiState

@Composable
fun HomeRoute(
    onNavigate: (Screen) -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    OnboardingNavigationEffect(
        currentScreen = Screen.Home,
        onboardingStep = uiState.onboardingStep,
        onNavigate = onNavigate
    )

    HomeScreen(
        uiState = uiState,
        onNavigate = onNavigate,
        onHomeNameChanged = viewModel::updateHomeNameInput,
        onSelectHome = viewModel::selectHome,
        onAddHome = viewModel::createHome
    )
}

@Composable
fun HomeScreen(
    uiState: HomeScreenUiState,
    onNavigate: (Screen) -> Unit,
    onHomeNameChanged: (String) -> Unit,
    onSelectHome: (Long) -> Unit,
    onAddHome: () -> Unit
) {
    val homesSubtitle = when (uiState.onboardingStep) {
        OnboardingStep.CreateHome -> "Onboarding starts with the very first home."
        OnboardingStep.AddRooms -> "Home is ready. The next onboarding step is adding a room."
        OnboardingStep.AddDevices -> "Home and room are ready. The next onboarding step is adding a device."
        OnboardingStep.Completed -> "The selected home defines rooms and devices."
    }

    SmartHomeScreen(
        currentScreen = Screen.Home,
        title = "Homes",
        subtitle = "Select a home or create the first one to start.",
        onboardingStep = uiState.onboardingStep,
        onNavigate = onNavigate
    ) {
        RoundedSection(
            title = "Your homes",
            subtitle = homesSubtitle
        ) {
            if (uiState.homes.isEmpty()) {
                EmptyState("No homes yet.")
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.homes, key = { it.id }) { home ->
                        FilterChip(
                            selected = uiState.selectedHome?.id == home.id,
                            onClick = { onSelectHome(home.id) },
                            label = { Text(home.name) },
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
                    value = uiState.homeNameInput,
                    onValueChange = onHomeNameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.canEditHomes,
                    label = { Text("Home name") },
                    placeholder = { Text("Example: Green House") },
                    shape = MaterialTheme.shapes.large
                )

                Button(
                    onClick = onAddHome,
                    enabled = uiState.canEditHomes,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (uiState.homes.isEmpty()) {
                            "Add first home"
                        } else {
                            "Add another home"
                        }
                    )
                }
            }
        }

        RoundedSection(
            title = "UI demo tiles",
            subtitle = "Four dead smart tiles for visual demonstration only."
        ) {
            SmartTilesGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp),
                tiles = previewHomeTiles(),
                onAction = {},
                userScrollEnabled = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHomeScreenCreateHome() {
    PreviewFrame {
        HomeScreen(
            uiState = previewHomeUiState(step = OnboardingStep.CreateHome),
            onNavigate = {},
            onHomeNameChanged = {},
            onSelectHome = {},
            onAddHome = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHomeScreenCompleted() {
    PreviewFrame {
        HomeScreen(
            uiState = previewHomeUiState(),
            onNavigate = {},
            onHomeNameChanged = {},
            onSelectHome = {},
            onAddHome = {}
        )
    }
}
