package com.example.practiceandroid.ext

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

// Функция для отрисовки кружка выбора
fun DrawScope.drawSelectionCircle(center: Offset) {
    // Белый кружок выбора
    drawCircle(
        color = Color.White,
        radius = 15f,
        center = center,
        style = Stroke(width = 14f)
    )

    // Черная обводка
    drawCircle(
        color = Color.Black,
        radius = 15f,
        center = center,
        style = Stroke(width = 7f)
    )
}
