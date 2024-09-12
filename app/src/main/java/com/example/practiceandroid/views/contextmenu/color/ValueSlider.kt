package com.example.practiceandroid.views.contextmenu.color

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.practiceandroid.ext.drawSelectionCircle

// Компонент для вертикального ползунка (Value)
@Composable
fun ValueSlider(
    hue: Float,
    saturation: Float,
    lightness: Float,
    onLightnessChange: (Float) -> Unit,
    isLightnessFieldsInitiator: MutableState<Boolean>
) {
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(200.dp)
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    val newValue = 1f - (tapOffset.y / size.height)
                    isLightnessFieldsInitiator.value = false
                    onLightnessChange(newValue.coerceIn(0f, 1f) * 100f)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val newValue = 1f - (change.position.y / size.height)
                    isLightnessFieldsInitiator.value = false
                    onLightnessChange(newValue.coerceIn(0f, 1f) * 100f)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.hsv(hue, saturation / 100f, 1f),
                        Color.Black
                    )
                )
            )

            val pointY = (1f - lightness / 100f) * size.height

            drawSelectionCircle(Offset(size.width / 2f, pointY))
        }
    }
}
