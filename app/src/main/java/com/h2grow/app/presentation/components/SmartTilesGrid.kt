package com.h2grow.app.presentation.components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import com.h2grow.app.domain.model.components.smartTile.SmartTile

@Composable
fun SmartTilesGrid(
    tile: List<SmartTile>,
    onAction: (TileAction) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(photos) { photo ->
            PhotoItem(photo)
        }
    }
}