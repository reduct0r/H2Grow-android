package com.h2grow.app.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.h2grow.app.presentation.navigation.AppNavigation
import com.h2grow.app.ui.theme.H2GrowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            H2GrowTheme {
                AppNavigation()
            }
        }
    }
}