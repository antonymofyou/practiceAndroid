package com.example.practiceandroid.views

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.practiceandroid.ext.decodeBase64ToBitmap
import com.example.practiceandroid.models.ResponseShapes
import kotlinx.coroutines.awaitCancellation
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


//Компонент для отрисовки фигур, focusManager - для сброса фокуса ввода текста
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawShapes(responseShapes: ResponseShapes, focusManager: FocusManager) {

    var scale by remember { mutableStateOf(1f) }

    var offset by remember { mutableStateOf(Offset.Zero) }

    var helper = remember { mutableStateOf(false) }
    var r = 1f
    val state = rememberTransformableState { scaleChange, offsetChange, _ ->
        // Обновляем масштаб с ограничением в пределах от 0.85f до 3f
        scale = (scale * scaleChange).coerceIn(0.85f, 3f)
    }

    BoxWithConstraints (
        modifier = Modifier
            .clipToBounds()
            .clickable { focusManager.clearFocus() }
    ){

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
                .clipToBounds()
                .transformable(state)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        if (helper.value == false){
                            scale = r
                        }
                        else{
                            helper.value = false
                        }
                        awaitFirstDown(pass = PointerEventPass.Initial)
                    }
                }

        ) {
            // Перебираем все фигуры в списке shapes, который был передан в responseShapes
            responseShapes.shapes.forEach { shape ->
                    // Определяем тип фигуры и вызываем соответствующую функцию для ее отрисовки
                    when (shape.type) {
                        "rectangle" -> DrawRectangle(shape, focusManager, maxWidth.value, maxHeight.value, helper)
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

