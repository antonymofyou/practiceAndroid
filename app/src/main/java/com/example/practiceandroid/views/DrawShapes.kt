package com.example.practiceandroid.views

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import com.example.practiceandroid.ext.decodeBase64ToBitmap
import com.example.practiceandroid.models.ResponseShapes

//Компонент для отрисовки фигур, focusManager - для сброса фокуса ввода текста
@Composable
fun DrawShapes(responseShapes: ResponseShapes, focusManager: FocusManager) {

    var scale by remember { mutableStateOf(1f) }

    var offset by remember { mutableStateOf(Offset.Zero) }

    BoxWithConstraints (
        modifier = Modifier
            .clipToBounds()
            .clickable { focusManager.clearFocus() }
    ){
        val state = rememberTransformableState { scaleChange, offsetChange, _ ->
            scale = (scale * scaleChange).coerceIn(0.85f, 3f)
        }

        BoxWithConstraints(
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
                .transformable(state)
                .clipToBounds()
        ) {
            // Перебираем все фигуры в списке shapes, который был передан в responseShapes
            responseShapes.shapes.forEach { shape ->
                    // Определяем тип фигуры и вызываем соответствующую функцию для ее отрисовки
                    when (shape.type) {
                        "rectangle" -> DrawRectangle(shape, focusManager, maxWidth.value, maxHeight.value)
                        "circle" -> DrawCircle(shape, focusManager, maxWidth.value, maxHeight.value)
                        "arrow" -> DrawArrow(shape, focusManager, maxWidth.value, maxHeight.value)
                        "image" -> DrawImage(
                            shape,
                            image = (responseShapes.imageDictionary[shape.imageId]!!).decodeBase64ToBitmap(),
                            focusManager, maxWidth.value, maxHeight.value
                        )
                    }
            }
        }
    }
}

