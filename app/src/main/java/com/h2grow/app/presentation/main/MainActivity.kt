package com.h2grow.app.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.h2grow.app.presentation.login.LoginScreen
import com.h2grow.app.ui.theme.H2GrowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            H2GrowTheme {
                LoginScreen()
            }
        }
    }
}