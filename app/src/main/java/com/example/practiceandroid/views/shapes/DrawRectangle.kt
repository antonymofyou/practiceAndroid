package com.example.practiceandroid.views.shapes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.practiceandroid.ext.valueOf
import com.example.practiceandroid.viewModels.shapesmodel.RectangleViewModel
import com.example.practiceandroid.views.contextmenu.border.ChangeSettingBorderDialog
import com.example.practiceandroid.views.contextmenu.color.ChangeColorDialog
import com.example.practiceandroid.views.contextmenu.ContextMenu
import com.example.practiceandroid.views.contextmenu.DeleteDialog
import com.example.practiceandroid.views.contextmenu.ResizeDialog
import kotlin.math.*

const val CHANGE_BACKGROUND_COLOR = "Изменить цвет фона"
const val CHANGE_BORDER_COLOR = "Изменить цвет границы"

// Компонент для отрисовки прямоугольника на основе данных, переданных в объекте shape
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawRectangle(
    rectangleViewModel: RectangleViewModel,
    focusManager: FocusManager,
    maxWidth: Float,
    maxHeight: Float,
) {
    // Границы элемента
    var top by remember { mutableFloatStateOf(0f) }
    var topR by remember { mutableFloatStateOf(0f) }
    var leftR by remember { mutableFloatStateOf(0f) }
    var left by remember { mutableFloatStateOf(0f) }
    var right by remember { mutableFloatStateOf(0f) }
    var bottom by remember { mutableFloatStateOf(0f) }

    // Дополнительные состояния для максимальных размеров
    var resizeMaxWidth by remember { mutableStateOf(maxWidth) }
    var resizeMaxHeight by remember { mutableStateOf(maxHeight) }

    // Глобальный оффсет элемента
    var rectangleOffsetInWindow = remember { mutableStateOf(Offset.Zero) }

    // Вспомогательная переменная для рекомпозиции
    var recomposition = 0f

    // Отслеживание позиции нажатия
    val touchOffset = remember { mutableStateOf(Offset.Zero) }

    val localDensity = LocalDensity.current

    // Контейнер Row для размещения текста внутри прямоугольника
    if (rectangleViewModel.visibility.value) {
        Box(
            modifier = Modifier
                .fillMaxSize() // Занимаем все пространство контейнера
        ) {
            Row(
                modifier = Modifier
                    .graphicsLayer(
                        rotationZ = rectangleViewModel.rotation.value,
                        scaleX = rectangleViewModel.scale.value,
                        scaleY = rectangleViewModel.scale.value,
                        translationX = rectangleViewModel.offset.value.x,
                        translationY = rectangleViewModel.offset.value.y,
                    )
                    // Устанавливаем фоновый цвет и закругленные углы для прямоугольника
                    .background(
                        color = Color(android.graphics.Color.parseColor(rectangleViewModel.color.value)),
                        shape = RoundedCornerShape(rectangleViewModel.cornerRadius.value)
                    )
                    .border(
                        width = rectangleViewModel.borderWidth.value / rectangleViewModel.scale.value,
                        color = Color(android.graphics.Color.parseColor(rectangleViewModel.borderColor.value)),
                        shape = RoundedCornerShape(rectangleViewModel.cornerRadius.value)
                    )
                    .width(rectangleViewModel.width.value)
                    .height(rectangleViewModel.height.value)
                    .zIndex(rectangleViewModel.zIndex.value)
                    .clickable { focusManager.clearFocus() }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, offsetChange, scaleChange, rotateChange ->

                            // Текущий масштаб
                            val currentScale = rectangleViewModel.scale.value

                            // Предполагаемый новый масштаб
                            val proposedScale = currentScale * scaleChange

                            // Вычисляем минимальный масштаб на основе текущих размеров фигуры
                            val minScaleWidth = 70f / rectangleViewModel.width.value.value
                            val minScaleHeight = 70f / rectangleViewModel.height.value.value
                            val minScale = maxOf(0.85f, minScaleWidth, minScaleHeight)

                            // Ограничиваем масштаб между minScale и 3f
                            val finalScale = proposedScale.coerceIn(minScale, 3f)

                            // Обновляем масштаб в ViewModel
                            rectangleViewModel.scale.value = finalScale

                            rectangleViewModel.rotation.value = (rectangleViewModel.rotation.value + rotateChange) % 360

                            // Рассчитываем косинус и синус угла вращения в радианах
                            val rotationRadians =
                                Math.toRadians(rectangleViewModel.rotation.value.toDouble())
                            val cosRotation = cos(rotationRadians).toFloat()
                            val sinRotation = sin(rotationRadians).toFloat()

                            // MinMax значения для смещения
                            val minOffsetX = -left
                            val minOffsetY = -top
                            val maxOffsetX = maxWidth - right
                            val maxOffsetY = maxHeight - bottom

                            var rotatedOffsetY = offsetChange.y * rectangleViewModel.scale.value
                            var rotatedOffsetX = offsetChange.x * rectangleViewModel.scale.value

                            // Применение вращения к смещению
                            var transformedOffsetX =
                                rotatedOffsetX * cosRotation - rotatedOffsetY * sinRotation
                            var transformedOffsetY =
                                rotatedOffsetX * sinRotation + rotatedOffsetY * cosRotation

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
                            rectangleViewModel.offset.value = Offset(
                                x = rectangleViewModel.offset.value.x + transformedOffsetX,
                                y = rectangleViewModel.offset.value.y + transformedOffsetY
                            )
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { offset ->
                                val rotationRadians =
                                    Math.toRadians(rectangleViewModel.rotation.value.toDouble())
                                val cosRotation = cos(rotationRadians).toFloat()
                                val sinRotation = sin(rotationRadians).toFloat()

                                // Рассчет локального оффсета на фигуре
                                var x =
                                    with(localDensity) { offset.x.toDp().value } * rectangleViewModel.scale.value
                                val y =
                                    with(localDensity) { offset.y.toDp().value } * rectangleViewModel.scale.value

                                // Применение вращения к смещению
                                var transformedOffsetX = x * cosRotation - y * sinRotation
                                var transformedOffsetY = x * sinRotation + y * cosRotation

                                touchOffset.value = Offset(
                                    (transformedOffsetX + rectangleOffsetInWindow.value.x),
                                    (transformedOffsetY + rectangleOffsetInWindow.value.y),
                                )
                                // Показываем контекстное меню
                                rectangleViewModel.showContextMenu.value = true
                            }
                        )
                    }
                    .onGloballyPositioned { layoutCoordinates ->
                        recomposition
                        val rect1 = layoutCoordinates.boundsInParent()
                        left = with(localDensity) { rect1.left.toDp().value }
                        top = with(localDensity) { rect1.top.toDp().value }
                        right = with(localDensity) { rect1.right.toDp().value }
                        bottom = with(localDensity) { rect1.bottom.toDp().value }
                        Log.e("DEBUG", "left: $left, right: $right, top: $top, bottom: $bottom")

                        // Рассчитываем косинус и синус угла вращения в радианах
                        val rotationRadians =
                            Math.toRadians(rectangleViewModel.rotation.value.toDouble())
                        val cosRotation = cos(rotationRadians).toFloat()
                        Log.e("COS", cosRotation.toString())
                        var w = with(localDensity) { layoutCoordinates.size.width.toDp().value}
                        var h = with(localDensity) { layoutCoordinates.size.height.toDp().value}
                        var maxL = left / cosRotation
                        var maxT = (maxHeight - bottom) / cosRotation
                        var maxR = (maxWidth - right) / cosRotation
                        var maxB = bottom / cosRotation

                        resizeMaxWidth = maxL + w + maxR
                        resizeMaxHeight = maxT + h + maxB
                        Log.e("DEBUG", "maxL: $maxL, maxR: $maxR, width: $w ${rectangleViewModel.rotation.value.toDouble()}")
                        Log.e("DEBUG", "resizeMaxWidth: $resizeMaxWidth")
//
                        val rect2 = layoutCoordinates.positionInWindow()
                        val x = with(localDensity) { rect2.x.toDp().value }
                        val y = with(localDensity) { rect2.y.toDp().value }
                        rectangleOffsetInWindow.value = Offset(x, y)
                    },
                verticalAlignment = Alignment.valueOf(rectangleViewModel.textVerticalAlignment)
            ) {
                // Перебираем текстовые блоки внутри фигуры
                rectangleViewModel.text?.forEach { textBlock ->
                    textBlock.text.forEachIndexed { index, textSegment ->
                        val text = remember { mutableStateOf(textSegment.text) }
                        BasicTextField(
                            value = text.value,
                            { text.value = it },
                            textStyle = TextStyle(
                                textAlign = TextAlign.valueOf(textBlock.alignment),
                                color = Color(android.graphics.Color.parseColor(textSegment.fontColor)),
                                fontSize = textSegment.fontSize.sp,
                                fontWeight = if (textSegment.type == "bold") FontWeight.Bold else FontWeight.Normal,
                                textDecoration = if (textSegment.textDecoration == "underline") TextDecoration.Underline else null
                            ),
                            modifier = Modifier.width(IntrinsicSize.Min),
                            singleLine = false,
                            enabled = true,
                        )
                        {
                            val interactionSource = remember { MutableInteractionSource() }
                            OutlinedTextFieldDefaults.DecorationBox(
                                value = text.value,
                                visualTransformation = VisualTransformation.None,
                                innerTextField = it,
                                singleLine = false,
                                enabled = true,
                                interactionSource = interactionSource,
                            )

                        }
                    }
                }
            }
        }
    }

    if (rectangleViewModel.showContextMenu.value) {
        ContextMenu(
            rectangleViewModel.showContextMenu,
            rectangleViewModel.showResizeDialog,
            rectangleViewModel.showDeleteDialog,
            rectangleViewModel.showChangeBackgroundColorDialog,
            rectangleViewModel.showChangeBorderSettingDialog,
            touchOffset.value,
        )
    }

    if (rectangleViewModel.showDeleteDialog.value) {
        DeleteDialog(rectangleViewModel.showDeleteDialog){
            rectangleViewModel.deleteShape()
        }
    }

    if (rectangleViewModel.showResizeDialog.value) {
        ResizeDialog(
            rectangleViewModel.showResizeDialog,
            rectangleViewModel.width,
            rectangleViewModel.height,
            rectangleViewModel.zIndex,
            rectangleViewModel.scale,
            resizeMaxWidth,
            resizeMaxHeight
        )
    }

    if (rectangleViewModel.showChangeBackgroundColorDialog.value) {
        ChangeColorDialog(
            rectangleViewModel.showChangeBackgroundColorDialog,
            rectangleViewModel.color,
            CHANGE_BACKGROUND_COLOR
        )
    }

    if (rectangleViewModel.showChangeBorderSettingDialog.value) {
        ChangeSettingBorderDialog(
            rectangleViewModel.showChangeBorderSettingDialog,
            rectangleViewModel.showChangeBorderColorDialog,
            rectangleViewModel.borderColor,
            rectangleViewModel.borderWidth,
            rectangleViewModel.cornerRadius
        )
    }

    if (rectangleViewModel.showChangeBorderColorDialog.value) {
        ChangeColorDialog(
            rectangleViewModel.showChangeBorderColorDialog,
            rectangleViewModel.borderColor,
            CHANGE_BORDER_COLOR
        )
    }
}




