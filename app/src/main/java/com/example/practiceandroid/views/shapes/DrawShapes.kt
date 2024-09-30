package com.example.practiceandroid.views.shapes

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practiceandroid.viewModels.shapesmodel.ArrowViewModel
import com.example.practiceandroid.viewModels.shapesmodel.ArrowViewModelFactory
import com.example.practiceandroid.viewModels.shapesmodel.CircleViewModel
import com.example.practiceandroid.viewModels.shapesmodel.CircleViewModelFactory
import com.example.practiceandroid.viewModels.shapesmodel.ImageViewModel
import com.example.practiceandroid.viewModels.shapesmodel.ImageViewModelFactory
import com.example.practiceandroid.viewModels.shapesmodel.RectangleViewModel
import com.example.practiceandroid.viewModels.shapesmodel.RectangleViewModelFactory
import com.example.practiceandroid.viewModels.shapesmodel.ShapesViewModel

//Компонент для отрисовки фигур, focusManager - для сброса фокуса ввода текста
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawShapes(
    shapesViewModel: ShapesViewModel,
    focusManager: FocusManager,
) {
    // Переменная, используемая для фильтрации первого ложного срабатывания при жестах масштабирования
    var firstFalsePositiveScale = true

    val localDensity = LocalDensity.current

    BoxWithConstraints (
        modifier = Modifier
            .clickable { focusManager.clearFocus() }
            .fillMaxSize()
            .clipToBounds()
    ){
        BoxWithConstraints(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = shapesViewModel.scale.value
                    scaleY = shapesViewModel.scale.value
                    translationX = shapesViewModel.offset.value.x
                    translationY = shapesViewModel.offset.value.y
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
                            shapesViewModel.scale.value =
                                (shapesViewModel.scale.value * scaleChange).coerceIn(0.85f, 1f)
                            // Обнуляем переменную для последующих жестов
                            firstFalsePositiveScale = false
                        }
                    }
                }
        ) {
            // Перебираем все фигуры в списке shapes, который был передан в responseShapes
            shapesViewModel.responseShapes.value!!.shapes.forEach { shape ->
                    // Определяем тип фигуры и вызываем соответствующую функцию для ее отрисовки
                    when (shape.type) {
                        "rectangle" -> {
                            val rectangleViewModel: RectangleViewModel = viewModel(
                                key = shape.id.toString(),
                                factory = RectangleViewModelFactory(shape)
                            )

                            DrawRectangle(rectangleViewModel, focusManager, maxWidth.value, maxHeight.value, localDensity)
                        }
                        "circle" -> {
                            val circleViewModel: CircleViewModel = viewModel(
                                key = shape.id.toString(),
                                factory = CircleViewModelFactory(shape)
                            )
                            DrawCircle(circleViewModel, focusManager, maxWidth.value, maxHeight.value, localDensity)
                        }
                        "arrow" -> {
                            val arrowViewModel: ArrowViewModel = viewModel(
                                key = shape.id.toString(),
                                factory = ArrowViewModelFactory(shape)
                            )

                            DrawArrow(arrowViewModel, focusManager, maxWidth.value, maxHeight.value, localDensity)
                        }
                        "image" -> {
                            val image = ( shapesViewModel.responseShapes.value!!.imageDictionary[shape.imageId]!!)
                            val imageViewModel: ImageViewModel = viewModel(
                                key = shape.id.toString(),
                                factory = ImageViewModelFactory(shape, image)
                            )

                            DrawImage(
                                imageViewModel, focusManager, maxWidth.value, maxHeight.value, localDensity
                            )
                        }
                    }
            }
        }
    }
}



