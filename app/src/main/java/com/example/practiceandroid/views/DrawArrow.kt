package com.example.practiceandroid.views


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontVariation.width
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

    // Создаем Path для стрелки, которая будет сохраняться неизменной
    val arrowPath = remember {
        Path().apply {
            moveTo(0f, 0f)
            // Рисуем линию до верхнего правого угла стержня стрелки
            lineTo(widthFloat, 0f)

            // Рисуем линию до верхней точки наконечника стрелки
            lineTo(widthFloat, 0f - arrowHeadHeightExtra.value)

            // Рисуем линию до острия наконечника стрелки
            lineTo(widthFloat * arrowHeadWidthExtra, heightFloat / 2f)

            // Рисуем линию до нижней точки наконечника стрелки
            lineTo(widthFloat, heightFloat + arrowHeadHeightExtra.value)

            // Рисуем линию до нижнего правого угла стержня стрелки
            lineTo(widthFloat, heightFloat)

            // Рисуем линию до нижнего левого угла стержня стрелки
            lineTo(0f, heightFloat)

            close()
        }
    }

    // Границы элемента
    var top by remember { mutableFloatStateOf(0f) }
    var left by remember { mutableFloatStateOf(0f) }
    var right by remember { mutableFloatStateOf(0f) }
    var bottom by remember { mutableFloatStateOf(0f) }

    // Вспомогательная переменная для рекомпозиции
    var recomposition = 0f

    val localDensity = LocalDensity.current

    Canvas(
        modifier = Modifier
            .graphicsLayer(
                rotationZ = shape.startRotation?.toFloat() ?: 0f,
                translationX = offset.x,
                translationY = offset.y,
                scaleY = scale,
                scaleX = scale
            )
            .zIndex(shape.zIndex)
            .clickable { focusManager.clearFocus() }
            .pointerInput(Unit) {
                detectTransformGestures { _, offsetChange, scaleChange, _ ->
                    // Обновляем масштаб с ограничением в пределах от 0.85f до 3f
                    scale = (scale * scaleChange).coerceIn(0.85f, 3f)

                    // Рассчитываем косинус и синус угла вращения в радианах
                    val rotationRadians = Math.toRadians(shape.startRotation?.toDouble() ?: 0.0)
                    val cosRotation = cos(rotationRadians).toFloat()
                    val sinRotation = sin(rotationRadians).toFloat()

                    // MinMax значения для смещения
                    val minOffsetX = -left
                    val minOffsetY = -top
                    val maxOffsetX = maxWidth - right
                    val maxOffsetY = maxHeight - bottom

                    var rotatedOffsetY = offsetChange.y * scale
                    var rotatedOffsetX = offsetChange.x * scale

                    // Применение вращения к смещению
                    var transformedOffsetX = rotatedOffsetX * cosRotation - rotatedOffsetY * sinRotation
                    var transformedOffsetY = rotatedOffsetX * sinRotation + rotatedOffsetY * cosRotation

                    // Ограничение смещения по оси X
                    transformedOffsetX = if (minOffsetX > maxOffsetX) {
                        transformedOffsetX.coerceIn(maxOffsetX, minOffsetX)
                    } else {
                        transformedOffsetX.coerceIn(minOffsetX, maxOffsetX)
                    }

                    // Ограничение смещения по оси Y
                    transformedOffsetY = if (minOffsetY > maxOffsetY) {
                        transformedOffsetY.coerceIn(maxOffsetY, minOffsetY)
                    } else {
                        transformedOffsetY.coerceIn(minOffsetY, maxOffsetY)
                    }

                    // Обновление значения смещения
                    offset = Offset(
                        x = offset.x + transformedOffsetX,
                        y = offset.y + transformedOffsetY
                    )
                }}
            .onGloballyPositioned { layoutCoordinates ->
                recomposition
                val rect = layoutCoordinates.boundsInParent()
                left = with(localDensity) { rect.left.toDp().value }
                top = with(localDensity) { rect.top.toDp().value }
                right = with(localDensity) { rect.right.toDp().value }
                bottom = with(localDensity) { rect.bottom.toDp().value }
            }
            .height(with(localDensity) {shape.height.toDp()})
            .width(with(localDensity) {shape.width.toDp()})
    ) {
            drawPath(
                path = arrowPath,
                color = Color(android.graphics.Color.parseColor(shape.fill))
            )
    }
}