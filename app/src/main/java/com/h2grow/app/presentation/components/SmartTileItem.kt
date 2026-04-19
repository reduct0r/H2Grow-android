package com.h2grow.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.h2grow.app.R
import com.h2grow.app.domain.model.components.smartTile.SmartTile
import com.h2grow.app.domain.model.components.smartTile.TileAction

@Composable
fun SmartTileItem(
    tile: SmartTile,
    onAction: (TileAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var multiplier by remember { mutableFloatStateOf(1f) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .aspectRatio(1.15f)
            .padding(8.dp)
            .clickable { onAction(TileAction.OpenDetails(tile.id)) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment  = Alignment.Start,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tile.icon?.let { iconId ->
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                    )
                }

                Text(
                    text = tile.title.orEmpty(),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 12.dp),
                    softWrap = false
                )
            }
            when (tile) {
                is SmartTile.Info -> {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {

                            val annotatedString = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontSize = 100.sp * multiplier)) {
                                    append(tile.value)
                                }
                                withStyle(style = SpanStyle(fontSize = 70.sp * multiplier)) {
                                    append(" ${tile.unit}")
                                }
                            }

                            Text(
                                text = annotatedString,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                softWrap = false,
                                maxLines = 1,
                                onTextLayout = { textLayoutResult ->
                                    if (textLayoutResult.hasVisualOverflow) {
                                        multiplier *= 0.65f
                                    }
                                }
                            )
                        }
                    }
                }

                is SmartTile.Toggle -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                            ) {

                            Switch(
                                checked  = tile.isOn,
                                onCheckedChange = { newValue -> onAction(TileAction.Toggle(tile.id, newValue)) }

                            )

                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = if (tile.isOn) "On" else "Off",
                                fontSize = 20.sp
                            )
                        }
                    }
                }

                is SmartTile.Dimmer -> {

                }
            }

        }
    }
}

@Preview
@Composable
fun PreviewSmartTileItemInfo(){
    SmartTileItem(
        tile = SmartTile.Info(
            id = "0",
            title = "Air Temperature",
            value = "15.2",
            unit = "°C",
            icon = R.drawable.ic_launcher_foreground
        ),
        onAction = { }
    )
}

@Preview
@Composable
fun PreviewSmartTileItemToggle() {
    SmartTileItem(
        tile = SmartTile.Toggle(
            id = "1",
            title = "Light Switch",
            isOn = false,
            needConfirm = true,
            icon = R.drawable.ic_launcher_background
        ),
        onAction = { }
    )
}