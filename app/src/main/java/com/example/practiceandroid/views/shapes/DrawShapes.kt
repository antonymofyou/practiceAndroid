package com.example.practiceandroid.views.shapes

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practiceandroid.models.ResponseShapes
import com.example.practiceandroid.viewModels.shapesmodel.ArrowViewModel
import com.example.practiceandroid.viewModels.shapesmodel.ArrowViewModelFactory
import com.example.practiceandroid.viewModels.shapesmodel.CircleViewModel
import com.example.practiceandroid.viewModels.shapesmodel.CircleViewModelFactory
import com.example.practiceandroid.viewModels.shapesmodel.DrawShapesViewModel
import com.example.practiceandroid.viewModels.shapesmodel.ImageViewModel
import com.example.practiceandroid.viewModels.shapesmodel.ImageViewModelFactory
import com.example.practiceandroid.viewModels.shapesmodel.RectangleViewModel
import com.example.practiceandroid.viewModels.shapesmodel.RectangleViewModelFactory

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

    BoxWithConstraints (
        modifier = Modifier
            .clickable { focusManager.clearFocus() }
            .fillMaxSize()
            .clipToBounds()
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
                .clipToBounds()
                // Добавил для отладки
                .border(4.dp, Color.Gray)
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

                            DrawRectangle(rectangleViewModel, focusManager, maxWidth.value, maxHeight.value)
                        }
                        "circle" -> {
                            val circleViewModel: CircleViewModel = viewModel(
                                key = shape.id.toString(),
                                factory = CircleViewModelFactory(shape)
                            )
                            DrawCircle(circleViewModel, focusManager, maxWidth.value, maxHeight.value)
                        }
                        "arrow" -> {
                            val arrowViewModel: ArrowViewModel = viewModel(
                                key = shape.id.toString(),
                                factory = ArrowViewModelFactory(shape)
                            )

                            DrawArrow(arrowViewModel, focusManager, maxWidth.value, maxHeight.value)
                        }
                        "image" -> {
                            val image = (responseShapes.imageDictionary[shape.imageId]!!)
                            val imageViewModel: ImageViewModel = viewModel(
                                key = shape.id.toString(),
                                factory = ImageViewModelFactory(shape, image)
                            )

                            DrawImage(
                                imageViewModel, focusManager, maxWidth.value, maxHeight.value
                            )
                        }
                    }
            }
        }
    }
}



