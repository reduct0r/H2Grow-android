package com.h2grow.app.domain.model.components.smartTile

sealed interface SmartTile {
    val id: String
    val title: String?

    data class Info(
        override val id: String,
        override val title: String?,
        val value: String,
        val unit: String,
        val icon: Int
    ): SmartTile

    data class Toggle(
        override val id: String,
        override val title: String?,
        val isOn: Boolean,
        val icon: Int,
        val needConfirm: Boolean = false
    ): SmartTile

    data class Dimmer(
        override val id: String,
        override val title: String?,
        val value: Double,
        val maxValue: Double = 0.0,
        val minValue: Double = 100.0,
        val unit: String = "%",
        val icon: Int,
        val needConfirm: Boolean = false
    ): SmartTile
}