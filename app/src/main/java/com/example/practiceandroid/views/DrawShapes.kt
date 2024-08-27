package com.example.practiceandroid.views

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.practiceandroid.ext.decodeBase64ToBitmap
import com.example.practiceandroid.models.ResponseShapes

@Composable
fun DrawShapes(responseShapes: ResponseShapes, focusManager: FocusManager) {

    var scale by remember { mutableStateOf(1f) }

    var offset by remember { mutableStateOf(Offset.Zero) }

    // Минимально допустимые значения для смещения
    var minOffsetX = 0f
    var minOffsetY = 0f

    BoxWithConstraints (
        modifier = Modifier
            .clipToBounds()
            .clickable { focusManager.clearFocus() }
    ){
        val localDensity = LocalDensity.current
        val state = rememberTransformableState { scaleChange, offsetChange, _ ->

            scale = (scale * scaleChange).coerceIn(0.85f, 3f)

            val maxOffsetX = maxWidth.value - maxWidth.value * scale - (-minOffsetX)
            val maxOffsetY = maxHeight.value - maxHeight.value * scale - (-minOffsetY)

            offset = if ((minOffsetX > maxOffsetX) || (minOffsetY > maxOffsetY)) {
                Offset(
                    x = offset.x + (offsetChange.x * scale * scale).coerceIn(maxOffsetX, minOffsetX),
                    y = offset.y + (offsetChange.y * scale * scale).coerceIn(maxOffsetY, minOffsetY)
                )
            }
            else {
                Offset(
                    x = offset.x + (offsetChange.x * scale).coerceIn(minOffsetX, maxOffsetX),
                    y = offset.y + (offsetChange.y * scale).coerceIn(minOffsetY, maxOffsetY)
                )
            }
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
                // Добавил для отладки
                .border(4.dp, Color.Gray)
                .onGloballyPositioned { coordinates ->
                    val positionInRoot = coordinates.positionInParent()
                    minOffsetX = with(localDensity) { -positionInRoot.x.toDp().value }
                    minOffsetY = with(localDensity) { -positionInRoot.y.toDp().value }
                }
                .transformable(state)
        ) {
            // Перебираем все фигуры в списке shapes, который был передан в responseShapes
            responseShapes.shapes.forEach { shape ->
                    // Определяем тип фигуры и вызываем соответствующую функцию для ее отрисовки
                    when (shape.type) {
                        "rectangle" -> DrawRectangle(shape, focusManager)
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

