package com.example.practiceandroid.views.contextmenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp



// Функция для проверки ширины
fun validateWidth(width: String, maxW: Float): Boolean {
    val widthValue = width.toFloatOrNull()
    return (widthValue != null) && (widthValue in 70f..maxW)
}

// Функция для проверки ширины
fun validateHeight(height: String, maxH: Float): Boolean {
    val heightValue = height.toFloatOrNull()
    return (heightValue != null) && (heightValue in 70f..maxH)
}

// Функция для проверки zIndex
fun validateZIndex(zIndex: String): Boolean {
    return zIndex.toFloatOrNull() != null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResizeDialog(
    showResizeDialog: MutableState<Boolean>,
    width: MutableState<Dp>,
    height: MutableState<Dp>,
    zIndex: MutableState<Float>,
    scale: MutableState<Float>,
    resizeMaxWidth: Float,
    resizeMaxHeight: Float,
) {
    var newWidth by rememberSaveable { mutableStateOf((width.value.value * scale.value).toString()) }
    var newHeight by rememberSaveable { mutableStateOf((height.value.value * scale.value).toString()) }
    var newZIndex by rememberSaveable { mutableStateOf((zIndex.value).toString()) }

    // Состояния для ошибок
    var widthError by rememberSaveable { mutableStateOf(false) }
    var heightError by rememberSaveable { mutableStateOf(false) }
    var zIndexError by rememberSaveable { mutableStateOf(false) }

    // Проверка активации кнопки: кнопка активна, когда нет ошибок
    val isConfirmButtonEnabled = !widthError && !heightError && !zIndexError

    AlertDialog(
        onDismissRequest = { showResizeDialog.value = false },
        confirmButton = {
            TextButton(onClick = {
                width.value = newWidth.toFloat().dp
                height.value = newHeight.toFloat().dp
                scale.value = 1f
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

            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(scrollState)
            ) {
                OutlinedTextField(
                    value = newWidth,
                    onValueChange = {
                        val filteredText = it.filter { it.isDigit() || it == '.' }
                        newWidth = filteredText
                        widthError = !validateWidth(newWidth, resizeMaxWidth)
                    },
                    label = { Text("Ширина (70-${resizeMaxWidth})") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    isError = widthError,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        errorBorderColor = Color.Red
                    ),
                    singleLine = true
                )
                OutlinedTextField(
                    value = newHeight,
                    onValueChange = {
                        val filteredText = it.filter { it.isDigit() || it == '.' }
                        newHeight = filteredText
                        heightError = !validateHeight(newHeight, resizeMaxHeight)
                    },
                    label = { Text("Высота (70-${resizeMaxHeight})") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    isError = heightError,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        errorBorderColor = Color.Red
                    ),
                    singleLine = true
                )
                OutlinedTextField(
                    value = newZIndex,
                    onValueChange = {
                        newZIndex = it
                        zIndexError = !validateZIndex(newZIndex)
                    },
                    label = { Text("Слой (zIndex)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    isError = zIndexError,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        errorBorderColor = Color.Red
                    ),
                    singleLine = true
                )
            }
        }
    )
}
