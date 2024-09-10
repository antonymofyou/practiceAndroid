package com.example.practiceandroid.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.practiceandroid.ext.drawSelectionCircle
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// Компонент для цветового круга
@Composable
fun ColorCircle(hue: Float, saturation: Float, onColorChange: (Float, Float) -> Unit) {

    var circleRadius by rememberSaveable { mutableStateOf(0f) }

    var circleCenterX by rememberSaveable { mutableStateOf(0f) }
    var circleCenterY by rememberSaveable { mutableStateOf(0f) }

    val pointPosition = Offset(
        x = circleCenterX + circleRadius * (saturation / 100f) * cos(hue * PI.toFloat() / 180),
        y = circleCenterY + circleRadius * (saturation / 100f) * sin(hue * PI.toFloat() / 180)
    )

    Box(
        modifier = Modifier
            .size(200.dp)
            .padding(5.dp)
            .onGloballyPositioned {
                val size = it.size
                circleRadius = minOf(size.width, size.height) / 2f
                circleCenterX = size.width / 2f
                circleCenterY = size.height / 2f
            }
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    val x = tapOffset.x - circleCenterX
                    val y = tapOffset.y - circleCenterY
                    val distance = sqrt(x * x + y * y)

                    val limitedDistance = distance.coerceAtMost(circleRadius)
                    val newHue = ((atan2(y, x) * 180 / PI).toFloat() + 360) % 360
                    val newSaturation = (limitedDistance / circleRadius) * 100f
                    onColorChange(newHue, newSaturation)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val x = change.position.x - circleCenterX
                    val y = change.position.y - circleCenterY
                    val distance = sqrt(x * x + y * y)

                    val limitedDistance = distance.coerceAtMost(circleRadius)
                    val newHue = ((atan2(y, x) * 180 / PI).toFloat() + 360) % 360
                    val newSaturation = (limitedDistance / circleRadius) * 100f
                    onColorChange(newHue, newSaturation)
                }
            }

    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            val colors = (0 until 360).map { angle ->
                Color.hsv(angle.toFloat(), 1f, 1f)
            }

            drawCircle(
                brush = Brush.sweepGradient(colors),
                radius = circleRadius,
                center = Offset(circleCenterX, circleCenterY)
            )
            if (pointPosition.x != 0f){
                drawSelectionCircle(pointPosition)
            }
        }
    }
}
