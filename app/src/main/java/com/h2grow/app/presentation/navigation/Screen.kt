package com.h2grow.app.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen : NavKey {

    @Serializable
    data object Login : Screen()

    @Serializable
    data object Register : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data object Initial : Screen()
}