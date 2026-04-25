package com.h2grow.app.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.h2grow.app.R
import com.h2grow.app.presentation.components.BottomAddButton
import com.h2grow.app.presentation.components.DropdownList
import com.h2grow.app.presentation.login.LoginScreen
import com.h2grow.app.presentation.login.LoginUiState
import com.h2grow.app.presentation.login.LoginViewModel

@Composable
fun HomeRoute(
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreen(
        uiState = uiState
    )
}

@Composable
fun HomeScreen(
    uiState: HomeScreenUiState,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            horizontalArrangement = Arrangement.Center
        ) {
            uiState.homesList?.let {
                DropdownList(
                    modifier = Modifier.padding(12.dp),
                    options = it,
                    label = stringResource(R.string.select_home),
                    selectedOption = uiState.selectedHome?.title ?: "No any home selected",
                    onOptionSelected = { },
                    onAddClick = { },
                    bottomContent = null
                )
            }

            if (uiState.homesList == null) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .height(50.dp),
                    onClick = { },
                ) {
                    Text(stringResource(R.string.add_home), fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewHomeScreen() {
    HomeScreen(
        uiState = HomeScreenUiState()
    )
}