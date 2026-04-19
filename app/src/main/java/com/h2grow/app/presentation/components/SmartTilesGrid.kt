package com.h2grow.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
        columns = GridCells.Adaptive(minSize = 188.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = tiles,
            key = { tile -> tile.id }
        ) { tile ->
            SmartTileItem(tile = tile, onAction = onAction)
        }
    }
}


@Preview(
    name = "SmartTilesGrid ",
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,orientation=portrait",
    showBackground = true
)
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
            ),
            SmartTile.Info(
                id = "2",
                title = "test",
                value = "55.2",
                unit = "PPM",
                icon = R.drawable.ic_launcher_foreground
            ),
            SmartTile.Toggle(
                id = "3",
                title = "test22",
                icon = R.drawable.ic_launcher_foreground,
                isOn = false
            )
        ),
        onAction = {}
    )
}