package com.example.practiceandroid.views


import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.practiceandroid.models.ResponseShapes

// Компонент для отрисовки стрелки на основе данных, переданных в объекте shape
@Composable
fun DrawArrow(shape: ResponseShapes.Shape) {
    // Преобразуем ширину и высоту стрелки в тип Float для дальнейшего использования
    var widthFloat = shape.width.toFloat()
    var heightFloat = shape.height.toFloat()

    // Дополнительная высота наконечника стрелки, которая добавляется к общей высоте стрелки
    val arrowHeadHeightExtra = 16.dp
    // Коэффициент, на который будет увеличена ширина стрелки для создания наконечника
    val arrowHeadWidthExtra = 1.25f

    Canvas(
        modifier = Modifier
            .graphicsLayer(
                translationX = shape.x + 100,
                translationY = shape.y + 100,
            )
            .zIndex(shape.zIndex)
    ) {
        val path = Path().apply {
            moveTo(0f, 0f)
            // Рисуем линию до верхнего правого угла стержня стрелки
            lineTo(widthFloat, 0f)

            // Рисуем линию до верхней точки наконечника стрелки
            lineTo(widthFloat, 0f - arrowHeadHeightExtra.toPx())

            // Рисуем линию до острия наконечника стрелки
            lineTo(widthFloat * arrowHeadWidthExtra, heightFloat / 2f)

            // Рисуем линию до нижней точки наконечника стрелки
            lineTo(widthFloat, heightFloat + arrowHeadHeightExtra.toPx())

            // Рисуем линию до нижнего правого угла стержня стрелки
            lineTo(widthFloat, heightFloat)

            // Рисуем линию до нижнего левого угла стержня стрелки
            lineTo(0f, heightFloat)

            close()
        }

        // Применяем смещение и вращение, затем рисуем путь
        translate(left = shape.x, top = shape.y) {
            rotate(degrees = shape.startRotation?.toFloat() ?: 0f, pivot = center) {
                drawPath(
                    path = path,
                    color = Color(android.graphics.Color.parseColor(shape.fill))
                )
            }
        }
    }
}