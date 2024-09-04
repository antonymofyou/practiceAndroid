package com.example.practiceandroid.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.practiceandroid.models.ResponseShapes
import kotlin.math.cos
import kotlin.math.sin

/**
    Компонент для отрисовки изображения, переданного через image и отрисовка с учетом
    параметров из shape
 */
@Composable
fun DrawImage(
    shape: ResponseShapes.Shape,
    image: ImageBitmap,
    focusManager: FocusManager,
    maxWidth: Float,
    maxHeight: Float
) {
    var scale by remember { mutableStateOf(1f) }

    var offset by remember { mutableStateOf(Offset(shape.x, shape.y)) }

    // Границы элемента
    var top by remember { mutableFloatStateOf(0f) }
    var left by remember { mutableFloatStateOf(0f) }
    var right by remember { mutableFloatStateOf(0f) }
    var bottom by remember { mutableFloatStateOf(0f) }

    // Вспомогательная переменная для рекомпозиции
    var recomposition = 0f

    val localDensity = LocalDensity.current

    Image(
        bitmap = image,
        contentDescription = null,
        modifier = Modifier
            .graphicsLayer(
                rotationZ = shape.rotation ?: 0f,
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y,
            )
            .width(shape.width.dp)
            .height(shape.height.dp)
            .zIndex(shape.zIndex)
            .clip(shape = RoundedCornerShape(shape.cornerRadius?.dp ?: 0.dp))
            .clickable { focusManager.clearFocus() }
            .pointerInput(Unit) {
                detectTransformGestures { _, offsetChange, scaleChange, _ ->
                    // Обновляем масштаб с ограничением в пределах от 0.85f до 3f
                    scale = (scale * scaleChange).coerceIn(0.85f, 3f)

                    // Рассчитываем косинус и синус угла вращения в радианах
                    val rotationRadians = Math.toRadians((shape.rotation?.toDouble() ?: 0.0))
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
    )
}