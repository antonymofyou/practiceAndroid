package com.example.practiceandroid.views.contextmenu.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Валидация для Hue (допустимый диапазон 0-360)
fun validateHue(hue: String): Boolean {
    val hueFloat = hue.toFloatOrNull()
    return hueFloat != null && hueFloat in 0f..360f
}

// Валидация для Saturation (допустимый диапазон 0-100)
fun validateSaturation(saturation: String): Boolean {
    val hueFloat = saturation.toFloatOrNull()
    return hueFloat != null && hueFloat in 0f..100f
}

// Валидация для Lightness (допустимый диапазон 0-100)
fun validateLightness(value: String): Boolean {
    val lightnessFloat = value.toFloatOrNull()
    return lightnessFloat != null && lightnessFloat in 0f..100f
}

fun getHexColorFromRGB(displayColor: Int): String {
    val red = android.graphics.Color.red(displayColor)
    val green = android.graphics.Color.green(displayColor)
    val blue = android.graphics.Color.blue(displayColor)
    return String.format("#%02X%02X%02X", red, green, blue)
}

@Composable
fun HSLColorPicker(
    initialColor: Int,
    isConfirmButtonEnabled: MutableState<Boolean>,
    onColorChange: (String) -> Unit
) {
    var hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(initialColor, hsv)

    var hue = rememberSaveable { mutableStateOf(hsv[0]) }
    var saturation = rememberSaveable { mutableStateOf(hsv[1] * 100) }
    var lightness = rememberSaveable { mutableStateOf(hsv[2] * 100) }

    var displayColor = android.graphics.Color.HSVToColor(
        floatArrayOf(hue.value, saturation.value / 100f, lightness.value / 100f)
    )

    var isHueFieldsInitiator = rememberSaveable { mutableStateOf(false) }
    var isSaturationFieldsInitiator = rememberSaveable { mutableStateOf(false) }
    var isLightnessFieldsInitiator = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(hue.value, saturation.value, lightness.value) {
        onColorChange(getHexColorFromRGB(displayColor))
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(top = 16.dp, end = 8.dp, start = 8.dp, bottom = 16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Квадрат для отображения цвета
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color(displayColor))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Цветовой круг и ползунок для Value
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            ColorCircle(
                hue = hue.value,
                saturation = saturation.value,
                onColorChange = { newHue, newSaturation ->
                    hue.value = newHue
                    saturation.value = newSaturation
                },
                isHueFieldsInitiator,
                isSaturationFieldsInitiator
            )

            Spacer(modifier = Modifier.width(16.dp))

            ValueSlider(
                hue = hue.value,
                saturation = saturation.value,
                lightness = lightness.value,
                onLightnessChange = { newValue ->
                    lightness.value = newValue
                },
                isLightnessFieldsInitiator
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        // Поля для ввода HSL
        HSLInputFields(
            hue = hue,
            saturation = saturation,
            lightness = lightness,
            isConfirmButtonEnabled,
            isHueFieldsInitiator,
            isSaturationFieldsInitiator,
            isLightnessFieldsInitiator
        )
    }
}