package com.example.practiceandroid.views

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practiceandroid.ext.decodeBase64ToBitmap
import com.example.practiceandroid.models.ResponseShapes
import com.example.practiceandroid.viewModels.DrawShapesViewModel
import com.example.practiceandroid.viewModels.RectangleViewModel
import com.example.practiceandroid.viewModels.RectangleViewModelFactory

//Компонент для отрисовки фигур, focusManager - для сброса фокуса ввода текста
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawShapes(
    responseShapes: ResponseShapes,
    focusManager: FocusManager,
    drawShapesViewModel: DrawShapesViewModel = viewModel(),
) {
    // Смещение для перемещения фигур по экрану
    var offset by remember { mutableStateOf(Offset.Zero) }

    // Переменная, используемая для фильтрации первого ложного срабатывания при жестах масштабирования
    var firstFalsePositiveScale = true

    val localDensity = LocalDensity.current

    BoxWithConstraints (
        modifier = Modifier
            .clipToBounds()
            .clickable { focusManager.clearFocus() }
    ){

        BoxWithConstraints(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = drawShapesViewModel.scale.value
                    scaleY = drawShapesViewModel.scale.value
                    translationX = offset.x
                    translationY = offset.y
                }
                .fillMaxSize()
                // Добавил для отладки
                .border(4.dp, Color.Gray)
                .clipToBounds()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        // Устанавливаем начальное значение переменной перед началом жеста
                        firstFalsePositiveScale = true
                        // Ждем первое касание пальцем на экране
                        awaitFirstDown(pass = PointerEventPass.Main)
                        firstFalsePositiveScale = false
                    }
                }
                .pointerInput(Unit){
                    detectTransformGestures { _, _, scaleChange, _ ->
                        // Если это не первый ложный жест, изменяем масштаб
                        if (firstFalsePositiveScale != true){
                            drawShapesViewModel.scale.value =
                                (drawShapesViewModel.scale.value * scaleChange).coerceIn(0.85f, 1f)
                            // Обнуляем переменную для последующих жестов
                            firstFalsePositiveScale = false
                        }
                    }
                }
        ) {
            // Перебираем все фигуры в списке shapes, который был передан в responseShapes
            responseShapes.shapes.forEach { shape ->
                    // Определяем тип фигуры и вызываем соответствующую функцию для ее отрисовки
                    when (shape.type) {
                        "rectangle" -> {
                            val rectangleViewModel: RectangleViewModel = viewModel(
                                key = shape.id.toString(),
                                factory = RectangleViewModelFactory(shape)
                            )
                            DrawRectangle(
                                rectangleViewModel,
                                focusManager,
                                maxWidth.value,
                                maxHeight.value,
                            )
                        }
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

