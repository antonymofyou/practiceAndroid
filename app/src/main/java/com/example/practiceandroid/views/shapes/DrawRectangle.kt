package com.example.practiceandroid.views.shapes

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
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
    // Вспомогательная переменная для рекомпозиции
    var recomposition = 0f

    // Отслеживание позиции нажатия
    val touchOffset = remember { mutableStateOf(Offset.Zero) }

    val localDensity = LocalDensity.current

    // Контейнер Row для размещения текста внутри прямоугольника
    if (rectangleViewModel.visibility.value) {
        Row(
            modifier = Modifier
                .graphicsLayer(
                    rotationZ = rectangleViewModel.rotation.value,
                    translationX = rectangleViewModel.offset.value.x,
                    translationY = rectangleViewModel.offset.value.y,
                )
                .border(
                    width = rectangleViewModel.borderWidth.value.value.dp, // Указываем width вместо Dp
                    color = Color(android.graphics.Color.parseColor(rectangleViewModel.borderColor.value)),
                    shape = RoundedCornerShape(rectangleViewModel.cornerRadius.value)
                )
                // Устанавливаем фоновый цвет и закругленные углы для прямоугольника
                .background(
                    color = Color(android.graphics.Color.parseColor(rectangleViewModel.color.value)),
                    shape = RoundedCornerShape(rectangleViewModel.cornerRadius.value)
                )
                .requiredWidth(rectangleViewModel.width.value)
                .requiredHeight(rectangleViewModel.height.value)
                .zIndex(rectangleViewModel.zIndex.value)
                .clickable { focusManager.clearFocus() }
                .pointerInput(Unit) {
                    detectTransformGestures { _, offsetChange, scaleChange, rotateChange ->

                        rectangleViewModel.isInitialUserSize.value = false

                        // Обновляем масштаб в ViewModel
                        rectangleViewModel.height.value = (rectangleViewModel.height.value * scaleChange)
                            .coerceIn(rectangleViewModel.minH.value, rectangleViewModel.maxH.value)
                        rectangleViewModel.width.value = (rectangleViewModel.width.value * scaleChange)
                            .coerceIn(rectangleViewModel.minW.value, rectangleViewModel.maxW.value)

                        rectangleViewModel.rotation.value = (rectangleViewModel.rotation.value + rotateChange) % 360

                        // Рассчитываем косинус и синус угла вращения в радианах
                        val rotationRadians =
                            Math.toRadians(rectangleViewModel.rotation.value.toDouble())
                        val cosRotation = cos(rotationRadians).toFloat()
                        val sinRotation = sin(rotationRadians).toFloat()

                        // MinMax значения для смещения
                        val minOffsetX = -rectangleViewModel.left.value
                        val minOffsetY = -rectangleViewModel.top.value
                        val maxOffsetX = maxWidth - rectangleViewModel.right.value
                        val maxOffsetY = maxHeight - rectangleViewModel.bottom.value

                        var rotatedOffsetY = offsetChange.y
                        var rotatedOffsetX = offsetChange.x

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
                                with(localDensity) { offset.x.toDp().value }
                            val y =
                                with(localDensity) { offset.y.toDp().value }

                            // Применение вращения к смещению
                            var transformedOffsetX = x * cosRotation - y * sinRotation
                            var transformedOffsetY = x * sinRotation + y * cosRotation

                            touchOffset.value = Offset(
                                (transformedOffsetX + rectangleViewModel.rectangleOffsetInWindow.value.x),
                                (transformedOffsetY + rectangleViewModel.rectangleOffsetInWindow.value.y),
                            )
                            // Показываем контекстное меню
                            rectangleViewModel.showContextMenu.value = true
                        }
                    )
                }
                .onGloballyPositioned { layoutCoordinates ->
                    recomposition

                    val bounds = layoutCoordinates.boundsInParent()
                    val leftBound = with(localDensity) { bounds.left.toDp().value }
                    val topBound = with(localDensity) { bounds.top.toDp().value }
                    val rightBound = with(localDensity) { bounds.right.toDp().value }
                    val bottomBound = with(localDensity) { bounds.bottom.toDp().value }

                    if(rectangleViewModel.isInitialUserSize.value) {
                        var offsetX = rectangleViewModel.offset.value.x
                        var offsetY = rectangleViewModel.offset.value.y

                        // Проверка и обновление смещения по оси X
                        if (leftBound < 0) {
                            offsetX += -leftBound
                        }
                        if (rightBound > maxWidth) {
                            offsetX -= (rightBound - maxWidth)
                        }

                        // Проверка и обновление смещения по оси Y
                        if (topBound < 0) {
                            offsetY += -topBound
                        }
                        if (bottomBound > maxHeight) {
                            offsetY -= (bottomBound - maxHeight)
                        }

                        // Присваиваем новое смещение
                        rectangleViewModel.offset.value = Offset(offsetX, offsetY)
                    }

                    // Обновление границ
                    val newBounds = layoutCoordinates.boundsInParent()
                    rectangleViewModel.left.value = with(localDensity) { newBounds.left.toDp().value }
                    rectangleViewModel.top.value = with(localDensity) { newBounds.top.toDp().value }
                    rectangleViewModel.right.value = with(localDensity) { newBounds.right.toDp().value }
                    rectangleViewModel.bottom.value = with(localDensity) { newBounds.bottom.toDp().value }

                    // Обновление позиции в окне
                    val windowPos = layoutCoordinates.positionInWindow()
                    rectangleViewModel.rectangleOffsetInWindow.value = with(localDensity) { Offset(windowPos.x.toDp().value, windowPos.y.toDp().value) }

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
            rectangleViewModel.minW,
            rectangleViewModel.maxW,
            rectangleViewModel.minH,
            rectangleViewModel.maxH,
            rectangleViewModel.isInitialUserSize
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




