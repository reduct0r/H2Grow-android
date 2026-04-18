package com.h2grow.app.domain.model.components

sealed interface SmartTile {
    val id: Long
    val title: String?

    data class Info(
        override val id: Long,
        override val title: String?,
        val value: String,
        val unit: String,
        val icon: Int
    ): SmartTile

    data class Toggle(
        override val id: Long,
        override val title: String?,
        val isOn: Boolean,
        val icon: Int
    ): SmartTile

    data class Dimmer(
        override val id: Long,
        override val title: String?,
        val value: Double,
        val maxValue: Double = 0.0,
        val minValue: Double = 100.0,
        val unit: String = "%",
        val icon: Int
    ): SmartTile
}