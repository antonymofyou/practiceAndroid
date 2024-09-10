package com.example.practiceandroid.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

// Валидация для Hue (допустимый диапазон 0-360)
fun validateHue(hue: String): Boolean {
    val hueValue = hue.toFloatOrNull()
    return hueValue != null && hueValue in 0f..360f
}

// Валидация для Saturation (допустимый диапазон 0-100)
fun validateSaturation(saturation: String): Boolean {
    val saturationValue = saturation.toFloatOrNull()
    return saturationValue != null && saturationValue in 0f..100f
}

// Валидация для Value (допустимый диапазон 0-100)
fun validateValue(value: String): Boolean {
    val valueValue = value.toFloatOrNull()
    return valueValue != null && valueValue in 0f..100f
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HSLInputFields(
    hue: MutableState<Float>,
    saturation: MutableState<Float>,
    value: MutableState<Float>,
    isConfirmButtonEnabled: MutableState<Boolean>
) {
    // Состояния для ошибок
    var hueError by rememberSaveable { mutableStateOf(false) }
    var saturationError by rememberSaveable { mutableStateOf(false) }
    var valueError by rememberSaveable { mutableStateOf(false) }

    var hueString = hue.value.roundToInt().toString()
    var saturationString = saturation.value.roundToInt().toString()
    var valueString = value.value.roundToInt().toString()

    isConfirmButtonEnabled.value = validateHue(hueString) && validateSaturation(saturationString) && validateValue(valueString)

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Поле для ввода Hue
        OutlinedTextField(
            value = hueString,
            onValueChange = {
                hueString = it.filter { char -> char.isDigit() || char == '.' }
                hueError = !validateHue(hueString)
                if (!hueError) {
                    hue.value = hueString.toFloat()
                }
            },
            label = { Text("H (0-360)") },
            isError = hueError,
            modifier = Modifier
                .weight(1f),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                errorBorderColor = Color.Red
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Поле для ввода Saturation
        OutlinedTextField(
            value = saturationString,
            onValueChange = {
                saturationString = it.filter { char -> char.isDigit() || char == '.' }
                saturationError = !validateSaturation(saturationString)
                if (!saturationError) {
                    saturation.value = saturationString.toFloat()
                }
            },
            label = { Text("S (0-100)") },
            isError = saturationError,
            modifier = Modifier
                .weight(1f),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                errorBorderColor = Color.Red
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Поле для ввода Value
        OutlinedTextField(
            value = valueString,
            onValueChange = {
                valueString = it.filter { char -> char.isDigit() || char == '.' }
                valueError = !validateValue(valueString)
                if (!valueError) {
                    value.value = valueString.toFloat()
                }
            },
            label = { Text("L (0-100)") },
            isError = valueError,
            modifier = Modifier
                .weight(1f),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                errorBorderColor = Color.Red
            ),
            singleLine = true
        )
    }
}

