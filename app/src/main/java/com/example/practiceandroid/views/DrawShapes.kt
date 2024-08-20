package com.example.practiceandroid.views

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.example.practiceandroid.ext.decodeBase64ToBitmap
import com.example.practiceandroid.models.ResponseShapes

@Composable
fun DrawShapes(responseShapes: ResponseShapes) {

    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val state = rememberTransformableState { scaleChange, offsetChange, _ ->
        // Обновляем масштабирование, учитывая текущее изменение масштаба
        scale *= scaleChange

        // Перемещение с учетом текущего масштаба
        offsetX += offsetChange.x * scale
        offsetY += offsetChange.y * scale
    }

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offsetX
                translationY = offsetY
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
                "image" -> DrawImage(shape, image = (responseShapes.imageDictionary[shape.imageId]!!).decodeBase64ToBitmap())
            }
        }
    }
}
