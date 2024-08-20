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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.practiceandroid.ext.decodeBase64ToBitmap
import com.example.practiceandroid.models.ResponseShapes

@Composable
fun DrawShapes(responseShapes: ResponseShapes) {

    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    var offset by remember { mutableStateOf(Offset.Zero) }


    BoxWithConstraints {
        val state = rememberTransformableState { scaleChange, offsetChange, _ ->
            // Обновляем масштабирование, учитывая текущее изменение масштаба
            scale = (scale * scaleChange).coerceIn(0.9f, 3f)

            // Вычисляем дополнительное пространство для смещения
            val extraWidth = (scale * maxWidth - maxWidth).coerceAtLeast(0.dp)
            val extraHeight = (scale * maxHeight - maxHeight).coerceAtLeast(0.dp)

            val maxX = extraWidth / 2f
            val maxY = extraHeight / 2f

            // Корректируем смещение с учетом масштабирования и ограничений
            offset = Offset(
                x = (offset.x + offsetChange.x * scale).coerceIn(-maxX.value, maxX.value),
                y = (offset.y + offsetChange.y * scale).coerceIn(-maxY.value, maxY.value)
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
