package com.example.practiceandroid.views

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
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

    //Делаем box размером равным максимальной ширине и длине фигур + смещение
    val boxWidth = (responseShapes.shapes.maxOf { it.x.dp.value}.dp + responseShapes.shapes.maxOf {it.width.dp.value }.dp)
    val boxHeight = (responseShapes.shapes.maxOf { it.y.dp.value}.dp + responseShapes.shapes.maxOf {it.height.dp.value }.dp)

    Box(
        modifier = Modifier
            .height(boxHeight)
            .width(boxWidth)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offsetX
                translationY = offsetY
            }
            .transformable(state)
    ) {
        // Перебираем все фигуры в списке shapes, который был передан в responseShapes
        responseShapes.shapes.forEach { shape ->
            // Определяем тип фигуры и вызываем соответствующую функцию для ее отрисовки
            when (shape.type) {
                "rectangle" -> DrawRectangle(shape)
                // Другие типы фигур (например, "circle", "image" и т.д.) могут быть добавлены сюда
            }
        }
    }
}
