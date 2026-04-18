package com.h2grow.app.domain.model.components.smartTile

sealed class TileAction {
    data class Toggle(
        val id: String,
        val newValue: Boolean
    ): TileAction()

    data class SetLevel(
        val id: String,
        val newLevel: Double
    ): TileAction()

    data class OpenDetails(
        val id: String,
    ): TileAction()
}