package com.example.practiceandroid.views.contextmenu.color

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
import androidx.compose.runtime.LaunchedEffect
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HSLInputFields(
    hue: MutableState<Float>,
    saturation: MutableState<Float>,
    lightness: MutableState<Float>,
    isConfirmButtonEnabled: MutableState<Boolean>,
    isHueFieldsInitiator: MutableState<Boolean>,
    isSaturationFieldsInitiator: MutableState<Boolean>,
    isLightnessFieldsInitiator: MutableState<Boolean>,
) {
    // Состояния для ошибок
    var hueError by rememberSaveable { mutableStateOf(false) }
    var saturationError by rememberSaveable { mutableStateOf(false) }
    var lightnessError by rememberSaveable { mutableStateOf(false) }

    var hueString by rememberSaveable { mutableStateOf(hue.value.roundToInt().toString()) }
    var saturationString by rememberSaveable { mutableStateOf(saturation.value.roundToInt().toString()) }
    var lightnessString by rememberSaveable { mutableStateOf(lightness.value.roundToInt().toString()) }

    LaunchedEffect(hue.value) {
        if (hueString.isNotEmpty()){
            hueString = hue.value.roundToInt().toString()
            hueError = false
        }
        else if (hueString.isEmpty() && !isHueFieldsInitiator.value){
            hueString = hue.value.roundToInt().toString()
        }
    }

    LaunchedEffect(saturation.value) {
        if (saturationString.isNotEmpty()){
            saturationString = saturation.value.roundToInt().toString()
            saturationError = false
        }
        else if (saturationString.isEmpty() && !isSaturationFieldsInitiator.value){
            saturationString = saturation.value.roundToInt().toString()
        }
    }

    LaunchedEffect(lightness.value) {
        if (lightnessString.isNotEmpty()){
            lightnessString = lightness.value.roundToInt().toString()
            lightnessError = false
        }
        else if (lightnessString.isEmpty() && !isLightnessFieldsInitiator.value){
            lightnessString = lightness.value.roundToInt().toString()
        }
    }

    isConfirmButtonEnabled.value = validateHue(hueString)
            && validateSaturation(saturationString)
            && validateLightness(lightnessString)

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
                if (validateHue(hueString)){
                    hue.value = hueString.toFloat()
                    hueError = false
                }
                else {
                    isHueFieldsInitiator.value = true
                    hueError = true
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
                if (validateSaturation(saturationString)){
                    saturation.value = saturationString.toFloat()
                    saturationError = false
                }
                else {
                    isSaturationFieldsInitiator.value = true
                    saturationError = true
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
            value = lightnessString,
            onValueChange = {
                lightnessString = it.filter { char -> char.isDigit() || char == '.' }
                if (validateLightness(lightnessString)){
                    lightness.value = lightnessString.toFloat()
                    lightnessError = false
                }
                else {
                    isLightnessFieldsInitiator.value = true
                    lightnessError = true
                }
            },
            label = { Text("L (0-100)") },
            isError = lightnessError,
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

