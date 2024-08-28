package com.example.practiceandroid.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
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
            .onGloballyPositioned { coordinates ->
                val positionInRoot = coordinates.positionInParent()
                minOffsetX = with(localDensity) { -positionInRoot.x.toDp().value }
                minOffsetY = with(localDensity) { -positionInRoot.y.toDp().value }
            }
            .transformable(state),
    )
}