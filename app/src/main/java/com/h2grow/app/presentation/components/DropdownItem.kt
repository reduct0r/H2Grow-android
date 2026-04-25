package com.h2grow.app.presentation.components

import com.h2grow.app.R

data class DropdownItem(
    val id: Long,
    val title: String,
    val iconRes: Int = R.drawable.ic_launcher_foreground
)