package com.example.practiceandroid.views.contextmenu.border

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorderAndCornerRadiusInputFields(
    selectedBorderWidth: MutableState<String>,
    selectedCornerRadius: MutableState<String>,
) {
    // Состояния для ошибок
    var borderWidthError by rememberSaveable { mutableStateOf(false) }
    var cornerRadiusError by rememberSaveable { mutableStateOf(false) }

    // Поле для ввода BorderWidth
    OutlinedTextField(
        value = selectedBorderWidth.value,
        onValueChange = {
            selectedBorderWidth.value = it.filter { char -> char.isDigit() || char == '.' }
            borderWidthError = !validateBorderWidth(selectedBorderWidth.value)
        },
        label = { Text("Ширина (0-10)") },
        isError = borderWidthError,
        modifier = Modifier
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            errorBorderColor = Color.Red
        ),
        singleLine = true
    )

    // Поле для ввода CornerRadius
    OutlinedTextField(
        value = selectedCornerRadius.value,
        onValueChange = {
            selectedCornerRadius.value = it.filter { char -> char.isDigit() || char == '.' }
            cornerRadiusError = !validateCornerRadius(selectedCornerRadius.value)
        },
        label = { Text("Радиус угла (0-70)") },
        isError = cornerRadiusError,
        modifier = Modifier
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            errorBorderColor = Color.Red
        ),
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorderAndCornerRadiusInputFields(
    selectedBorderWidth: MutableState<String>
) {
    // Состояния для ошибок
    var borderWidthError by rememberSaveable { mutableStateOf(false) }

    // Поле для ввода BorderWidth
    OutlinedTextField(
        value = selectedBorderWidth.value,
        onValueChange = {
            selectedBorderWidth.value = it.filter { char -> char.isDigit() || char == '.' }
            borderWidthError = !validateBorderWidth(selectedBorderWidth.value)
        },
        label = { Text("Ширина (0-10)") },
        isError = borderWidthError,
        modifier = Modifier
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            errorBorderColor = Color.Red
        ),
        singleLine = true
    )
}