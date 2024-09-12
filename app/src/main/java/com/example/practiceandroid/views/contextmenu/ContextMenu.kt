package com.example.practiceandroid.views.contextmenu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun ContextMenu(
    showContextMenu: MutableState<Boolean>,
    showResizeDialog: MutableState<Boolean>,
    showDeleteDialog: MutableState<Boolean>,
    showChangeBackgroundColorDialog: MutableState<Boolean>,
    showChangeBorderSettingDialog: MutableState<Boolean>,
    offset: Offset,
) {
        DropdownMenu(
            expanded = showContextMenu.value,
            onDismissRequest = { showContextMenu.value = false },
            offset = DpOffset(offset.x.dp, offset.y.dp)
        ) {
            DropdownMenuItem(
                text = { Text("Удалить") },
                onClick = {
                    showDeleteDialog.value = true
                    showContextMenu.value = false
                },
                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = "Удалить") }
            )
            DropdownMenuItem(
                text = { Text("Изменить размер") },
                onClick = {
                    showResizeDialog.value = true
                    showContextMenu.value = false
                },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "Изменить размер") }
            )
            DropdownMenuItem(
                text = { Text("Изменить цвет фона") },
                onClick = {
                    showChangeBackgroundColorDialog.value = true
                    showContextMenu.value = false
                },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "Изменить цвет фона") }
            )
            DropdownMenuItem(
                text = { Text("Изменить параметры границы") },
                onClick = {
                    showChangeBorderSettingDialog.value = true
                    showContextMenu.value = false
                },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "Изменить цвет границы") }
            )
        }
}

@Composable
fun ContextMenu(
    showContextMenu: MutableState<Boolean>,
    showResizeDialog: MutableState<Boolean>,
    showDeleteDialog: MutableState<Boolean>,
    showChangeBorderSettingDialog: MutableState<Boolean>,
    offset: Offset,
) {
    DropdownMenu(
        expanded = showContextMenu.value,
        onDismissRequest = { showContextMenu.value = false },
        offset = DpOffset(offset.x.dp, offset.y.dp)
    ) {
        DropdownMenuItem(
            text = { Text("Удалить") },
            onClick = {
                showDeleteDialog.value = true
                showContextMenu.value = false
            },
            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = "Удалить") }
        )
        DropdownMenuItem(
            text = { Text("Изменить размер") },
            onClick = {
                showResizeDialog.value = true
                showContextMenu.value = false
            },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "Изменить размер") }
        )
        DropdownMenuItem(
            text = { Text("Изменить параметры границы") },
            onClick = {
                showChangeBorderSettingDialog.value = true
                showContextMenu.value = false
            },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "Изменить цвет границы") }
        )
    }
}