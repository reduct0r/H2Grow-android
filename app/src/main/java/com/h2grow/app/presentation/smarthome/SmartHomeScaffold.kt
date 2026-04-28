package com.h2grow.app.presentation.smarthome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.h2grow.app.presentation.home.OnboardingStep
import com.h2grow.app.presentation.navigation.Screen

@Composable
fun SmartHomeScreen(
    currentScreen: Screen,
    title: String,
    subtitle: String,
    onboardingStep: OnboardingStep,
    onNavigate: (Screen) -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            MainBottomBar(
                currentScreen = currentScreen,
                onboardingStep = onboardingStep,
                onNavigate = onNavigate
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (onboardingStep != OnboardingStep.Completed) {
                OnboardingCard(step = onboardingStep)
            }

            content()
        }
    }
}

@Composable
fun RoundedSection(
    title: String,
    subtitle: String? = null,
    contentPadding: PaddingValues = PaddingValues(18.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            content()
        }
    }
}

@Composable
fun EmptyState(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun OnboardingCard(step: OnboardingStep) {
    val message = when (step) {
        OnboardingStep.CreateHome -> "Step 1 of 3. Add the first home."
        OnboardingStep.AddRooms -> "Step 2 of 3. Create the first room."
        OnboardingStep.AddDevices -> "Step 3 of 3. Add a virtual device stub."
        OnboardingStep.Completed -> ""
    }

    RoundedSection(
        title = "Onboarding",
        subtitle = message
    ) {
        Text(
            text = "While onboarding is active, navigation and unrelated actions stay disabled.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MainBottomBar(
    currentScreen: Screen,
    onboardingStep: OnboardingStep,
    onNavigate: (Screen) -> Unit
) {
    val items = listOf(
        Screen.Home to "Homes",
        Screen.Rooms to "Rooms",
        Screen.Device to "Device",
        Screen.Scenarios to "Scenarios"
    )

    NavigationBar {
        items.forEach { (screen, label) ->
            NavigationBarItem(
                selected = currentScreen == screen,
                onClick = { onNavigate(screen) },
                enabled = isNavigationEnabled(
                    onboardingStep = onboardingStep,
                    target = screen
                ),
                icon = {},
                label = { Text(label) }
            )
        }
    }
}

private fun isNavigationEnabled(
    onboardingStep: OnboardingStep,
    target: Screen
): Boolean = when (onboardingStep) {
    OnboardingStep.CreateHome -> target == Screen.Home
    OnboardingStep.AddRooms -> target == Screen.Rooms
    OnboardingStep.AddDevices -> target == Screen.Device
    OnboardingStep.Completed -> target in setOf(
        Screen.Home,
        Screen.Rooms,
        Screen.Device,
        Screen.Scenarios
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewSmartHomeScreen() {
    PreviewFrame {
        SmartHomeScreen(
            currentScreen = Screen.Home,
            title = "Homes",
            subtitle = "Preview of the common scaffold.",
            onboardingStep = OnboardingStep.AddRooms,
            onNavigate = {}
        ) {
            RoundedSection(
                title = "Content section",
                subtitle = "Shared layout preview"
            ) {
                Text("This is a sample body.")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRoundedSection() {
    PreviewFrame {
        RoundedSection(
            title = "Rounded section",
            subtitle = "Reusable card block"
        ) {
            Text("Preview content")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEmptyState() {
    PreviewFrame {
        EmptyState("Nothing here yet.")
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewOnboardingCardAddRooms() {
    PreviewFrame {
        OnboardingCard(step = OnboardingStep.AddRooms)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewOnboardingCardAddDevices() {
    PreviewFrame {
        OnboardingCard(step = OnboardingStep.AddDevices)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMainBottomBar() {
    PreviewFrame {
        MainBottomBar(
            currentScreen = Screen.Device,
            onboardingStep = OnboardingStep.Completed,
            onNavigate = {}
        )
    }
}
