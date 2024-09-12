package com.example.practiceandroid.views.contextmenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ResizeDialog(
    showResizeDialog: MutableState<Boolean>,
    width: MutableState<Dp>,
    height: MutableState<Dp>,
    zIndex: MutableState<Float>,
    scale: MutableState<Float>
) {
    var newWidth by rememberSaveable { mutableStateOf((width.value.value * scale.value).toString()) }
    var newHeight by rememberSaveable { mutableStateOf((height.value.value * scale.value).toString()) }
    var newZIndex by rememberSaveable { mutableStateOf((zIndex.value).toString()) }

    val isConfirmButtonEnabled = newWidth.isNotEmpty() && newHeight.isNotEmpty() && newZIndex.isNotEmpty()

    AlertDialog(
        onDismissRequest = {
            showResizeDialog.value = false },
        confirmButton = {
            TextButton(onClick = {
                width.value = newWidth.toFloat().dp
                height.value = newHeight.toFloat().dp
                zIndex.value = newZIndex.toFloat()
                showResizeDialog.value = false
            },
                enabled = isConfirmButtonEnabled) {
                Text("ОК")
            }
        },
        dismissButton = {
            TextButton(onClick = { showResizeDialog.value = false }) {
                Text("Отмена")
            }
        },
        title = { Text("Изменить размеры и слой") },
        text = {
            val scrollState = rememberScrollState()

            Column (
                modifier = Modifier.fillMaxWidth().verticalScroll(scrollState)
            ){
                OutlinedTextField(
                    value = newWidth,
                    onValueChange = {
                        val filteredText = it.filter { it.isDigit() || it == '.'}
                        newWidth = filteredText},
                    label = { Text("Ширина") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                OutlinedTextField(
                    value = newHeight,
                    onValueChange = {
                        val filteredText = it.filter { it.isDigit() || it == '.'}
                        newHeight = filteredText },
                    label = { Text("Высота") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = newZIndex,
                    onValueChange = { newZIndex = it},
                    label = { Text("Слой (zIndex)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    )
}
