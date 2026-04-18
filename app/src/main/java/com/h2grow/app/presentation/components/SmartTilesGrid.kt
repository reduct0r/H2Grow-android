package com.h2grow.app.presentation.components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.h2grow.app.R
import com.h2grow.app.domain.model.components.smartTile.SmartTile
import com.h2grow.app.domain.model.components.smartTile.TileAction

@Composable
fun SmartTilesGrid(
    tiles: List<SmartTile>,
    onAction: (TileAction) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(
            items = tiles,
            key = { tile -> tile.id }
        ) { tile ->
            SmartTileItem(tile = tile, onAction = onAction)
        }
    }
}


@Preview
@Composable
fun PreviewSmartTilesGrid() {
    SmartTilesGrid(
        tiles = listOf(
            SmartTile.Info(
                id = "0",
                title = "test",
                value = "55.2",
                unit = "PPM",
                icon = R.drawable.ic_launcher_foreground
        )
        ),
        onAction = {}
    )
}