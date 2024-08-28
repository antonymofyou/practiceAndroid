package com.example.practiceandroid.views


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.practiceandroid.models.ResponseShapes
import kotlin.math.cos
import kotlin.math.sin

// Компонент для отрисовки стрелки на основе данных, переданных в объекте shape
@Composable
fun DrawArrow(shape: ResponseShapes.Shape, focusManager: FocusManager, maxWidth: Float, maxHeight: Float) {
    // Преобразуем ширину и высоту стрелки в тип Float для дальнейшего использования
    var widthFloat = shape.width.toFloat()
    var heightFloat = shape.height.toFloat()

    // Дополнительная высота наконечника стрелки, которая добавляется к общей высоте стрелки
    val arrowHeadHeightExtra = 16.dp
    // Коэффициент, на который будет увеличена ширина стрелки для создания наконечника
    val arrowHeadWidthExtra = 1.25f

    var scale by remember { mutableStateOf(1f) }

    var offset by remember { mutableStateOf(Offset(shape.x, shape.y)) }

    // Минимально допустимые значения для смещения
    var minOffsetX = -offset.x
    var minOffsetY = -offset.y

    val localDensity = LocalDensity.current
    val state = rememberTransformableState { scaleChange, offsetChange, _ ->
        // Обновляем масштаб с ограничением в пределах от 0.85f до 3f
        scale = (scale * scaleChange).coerceIn(0.85f, 3f)

        // Рассчитываем косинус и синус угла вращения в радианах
        val rotationRadians = Math.toRadians((shape.rotation?.toDouble() ?: 0.0))
        val cosRotation = cos(rotationRadians).toFloat()
        val sinRotation = sin(rotationRadians).toFloat()

        // Рассчитываем минимальные и максимальные смещения с учетом вращения
        val rotatedMinOffsetX = minOffsetX * cosRotation - minOffsetY * sinRotation
        val rotatedMinOffsetY = minOffsetX * sinRotation + minOffsetY * cosRotation

        val scaledWidth = shape.width * scale
        val scaledHeight = shape.height * scale

        val rotatedScaledWidthX = scaledWidth * cosRotation - scaledWidth * sinRotation
        val rotatedScaledHeightY = scaledHeight * sinRotation + scaledHeight * cosRotation

        val maxOffsetX = maxWidth - rotatedScaledWidthX - (-rotatedMinOffsetX)
        val maxOffsetY = maxHeight - rotatedScaledHeightY - (-rotatedMinOffsetY)

        // Ограничение смещения по оси Y
        val rotatedOffsetY = if (minOffsetY > maxOffsetY) {
            (offsetChange.y * scale).coerceIn(maxOffsetY, minOffsetY)
        } else {
            (offsetChange.y * scale).coerceIn(minOffsetY, maxOffsetY)
        }

        // Ограничение смещения по оси X
        val rotatedOffsetX = if (minOffsetX > maxOffsetX) {
            (offsetChange.x * scale).coerceIn(maxOffsetX, minOffsetX)
        } else {
            (offsetChange.x * scale).coerceIn(minOffsetX, maxOffsetX)
        }

        // Применение вращения к смещению
        val transformedOffsetX = rotatedOffsetX * cosRotation - rotatedOffsetY * sinRotation
        val transformedOffsetY = rotatedOffsetX * sinRotation + rotatedOffsetY * cosRotation

        // Обновление значения смещения
        offset = Offset(
            x = offset.x + transformedOffsetX,
            y = offset.y + transformedOffsetY
        )

    }

    Canvas(
        modifier = Modifier
            .graphicsLayer(
                rotationZ = shape.startRotation?.toFloat() ?: 0f,
            )
            .zIndex(shape.zIndex)
            .clickable { focusManager.clearFocus() }
            .onGloballyPositioned { coordinates ->
                val positionInRoot = coordinates.positionInParent()
                minOffsetX = with(localDensity) { -positionInRoot.x.toDp().value }
                minOffsetY = with(localDensity) { -positionInRoot.y.toDp().value }
            }
            .transformable(state)
            .height(shape.height.dp)
            .width(shape.width.dp)
    ) {
        withTransform({
            translate(offset.x, offset.y)
            scale(scale)
        }) {
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

            drawPath(
                path = path,
                color = Color(android.graphics.Color.parseColor(shape.fill))
            )
        }
    }
}