package com.h2grow.app.presentation.components

import android.widget.SeekBar
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
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
import com.h2grow.app.domain.model.components.smartTile.TileAction.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartTileItem(
    tile: SmartTile,
    onAction: (TileAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var multiplier by remember { mutableFloatStateOf(1f) }
    var sliderValue by remember { mutableFloatStateOf(0f) }

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
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment  = Alignment.Start,
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tile.icon?.let { iconId ->
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = null,
                        modifier = modifier.size(50.dp),
                    )
                }

                Text(
                    text = tile.title.orEmpty(),
                    fontSize = 24.sp,
                    modifier = modifier.padding(start = 12.dp),
                    softWrap = false
                )
            }
            when (tile) {
                is SmartTile.Info -> {
                    Column(
                        modifier = modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {

                        Row(
                            modifier = modifier.fillMaxWidth(),
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
                                modifier = modifier.fillMaxWidth(),
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
                        modifier = modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                            ) {

                            Switch(
                                checked  = tile.isOn,
                                onCheckedChange = { newValue -> onAction(Toggle(tile.id, newValue)) }

                            )

                            Text(
                                modifier = modifier.padding(start = 8.dp),
                                text = if (tile.isOn) "On" else "Off",
                                fontSize = 20.sp
                            )
                        }
                    }
                }

                is SmartTile.Dimmer -> {
                    sliderValue = tile.value.toFloat()

                    Column(
                        modifier = modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                modifier = modifier.padding(10.dp),
                                text = sliderValue.toString(),
                                fontSize = 30.sp
                            )
                            Text(
                                text = tile.unit,
                                fontSize = 30.sp
                            )
                        }

                        Row(
                            modifier = modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {

                            Slider(
                                value = sliderValue,
                                onValueChange = { newValue ->
                                    onAction(SetLevel(tile.id, newValue.toDouble()))
                                    sliderValue = newValue
                                },
                                valueRange = tile.maxValue.toFloat()..tile.minValue.toFloat(),
                                steps = 100,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = SliderDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.primary,
                                    activeTrackColor = MaterialTheme.colorScheme.primary,
                                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                                    activeTickColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }

                }

                is SmartTile.EmptyAdd -> TODO()
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

@Preview
@Composable
fun PreviewSmartTileItemDimmer() {
    SmartTileItem(
        tile = SmartTile.Dimmer(
            id = "1",
            title = "Light Dimmer",
            value = 50.0,
            icon = R.drawable.ic_launcher_foreground,
        ),
        onAction = { }
    )
}