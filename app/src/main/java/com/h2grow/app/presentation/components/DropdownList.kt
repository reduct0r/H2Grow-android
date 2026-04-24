package com.h2grow.app.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.h2grow.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownList(
    modifier: Modifier = Modifier,
    options: List<String>,
    label: String,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onAddClick: (() -> Unit)? = null,
    bottomContent: (@Composable () -> Unit)? = null
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier.clip(RoundedCornerShape(24.dp)),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        TextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            options.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onOptionSelected(item)
                        expanded = false
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(24.dp))
                        )
                    }
                )
            }

            if (onAddClick != null || bottomContent != null) {
                HorizontalDivider()
            }

            bottomContent?.invoke()

            onAddClick?.let {
                BottomAddButton(
                    onClick = {
                        it()
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun BottomAddButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        TextButton(onClick = onClick) {
            Text("➕ Add")
        }
    }
}

@Preview
@Composable
fun PreviewDropDownList() {
    var selected by remember { mutableStateOf("Первый") }

    DropdownList(
        options = listOf("Первый", "Второй", "Третий"),
        label = "Выберите элемент",
        selectedOption = selected,
        onOptionSelected = { selected = it },
        bottomContent = {
            BottomAddButton(onClick = {
                Log.d("PreviewDropDownList", "Добавить")
            })
        }
    )
}