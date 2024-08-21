package com.example.practiceandroid.views

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import com.example.practiceandroid.ext.decodeBase64ToBitmap
import com.example.practiceandroid.models.ResponseShapes

@Composable
fun DrawShapes(responseShapes: ResponseShapes) {

    var scale by remember { mutableStateOf(1f) }

    var offset by remember { mutableStateOf(Offset.Zero) }

    // Минимально допустимые значения для смещения
    var minOffsetX = 0f
    var minOffsetY = 0f

    BoxWithConstraints (
    ){
        val localDensity = LocalDensity.current
        val state = rememberTransformableState { scaleChange, offsetChange, _ ->

            // Проверяем, выходим ли за границы Root view
            if ((maxWidth.value * (scale * scaleChange) <= maxWidth.value) &&
                (maxHeight.value * (scale * scaleChange) <= maxHeight.value)){
                scale = (scale * scaleChange).coerceIn(0.5f, 3f)
            }

            // Максимально допустимые значения для смещения
            val maxOffsetX = maxWidth.value - maxWidth.value * scale - (-minOffsetX)
            val maxOffsetY = maxHeight.value - maxHeight.value * scale - (-minOffsetY)

            offset = Offset(
                x = offset.x + (offsetChange.x * scale).coerceIn(minOffsetX, maxOffsetX),
                y = offset.y + (offsetChange.y * scale).coerceIn(minOffsetY, maxOffsetY)
            )
        }

        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    val positionInRoot = coordinates.positionInRoot()
                    minOffsetX = with(localDensity) { -positionInRoot.x.toDp().value }
                    minOffsetY = with(localDensity) { -positionInRoot.y.toDp().value }
                }
                .transformable(state)
        ) {
            // Перебираем все фигуры в списке shapes, который был передан в responseShapes
            responseShapes.shapes.forEach { shape ->
                // Определяем тип фигуры и вызываем соответствующую функцию для ее отрисовки
                when (shape.type) {
                    "rectangle" -> DrawRectangle(shape)
                    "circle" -> DrawCircle(shape)
                    "arrow" -> DrawArrow(shape)
                    "image" -> DrawImage(
                        shape,
                        image = (responseShapes.imageDictionary[shape.imageId]!!).decodeBase64ToBitmap()
                    )
                }
            }
        }
    }
}
