package com.example.practiceandroid.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.practiceandroid.models.ResponseShapes

@Composable
fun DrawShapes(responseShapes: ResponseShapes) {
    Box(
        modifier = Modifier.fillMaxSize()
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
