package com.example.practiceandroid.views.shapes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.practiceandroid.ext.valueOf
import com.example.practiceandroid.viewModels.shapesmodel.CircleViewModel
import com.example.practiceandroid.views.contextmenu.border.ChangeSettingBorderDialog
import com.example.practiceandroid.views.contextmenu.color.ChangeColorDialog
import com.example.practiceandroid.views.contextmenu.ContextMenu
import com.example.practiceandroid.views.contextmenu.DeleteDialog
import com.example.practiceandroid.views.contextmenu.ResizeDialog
import kotlin.math.cos
import kotlin.math.sin

// Компонент для отрисовки круга на основе данных, переданных в объекте shape
@Composable
fun DrawCircle(circleViewModel: CircleViewModel, focusManager: FocusManager, maxWidth: Float, maxHeight: Float) {

    // Вспомогательная переменная для рекомпозиции
    var recomposition = 0f

    // Отслеживание позиции нажатия
    val touchOffset = remember { mutableStateOf(Offset.Zero) }

    val localDensity = LocalDensity.current
    if (circleViewModel.visibility.value) {
        Row(
            modifier = Modifier
                .graphicsLayer(
                    rotationZ = circleViewModel.rotation.value,
                    translationX = circleViewModel.offset.value.x,
                    translationY = circleViewModel.offset.value.y
                )
                .background(
                    color = Color(android.graphics.Color.parseColor(circleViewModel.color.value)),
                    shape = RoundedCornerShape(circleViewModel.cornerRadius.value)
                )
                .border(
                    width = circleViewModel.borderWidth.value,
                    color = Color(android.graphics.Color.parseColor(circleViewModel.borderColor.value)),
                    shape = RoundedCornerShape(circleViewModel.cornerRadius.value)
                )
                .width(circleViewModel.width.value)
                .height(circleViewModel.height.value)
                .zIndex(circleViewModel.zIndex.value)
                .clickable { focusManager.clearFocus() }
                .pointerInput(Unit) {
                    detectTransformGestures { _, offsetChange, scaleChange, rotateChange ->

                        circleViewModel.isInitialUserSize.value = false

                        // Текущие значения высоты и ширины
                        val currentHeight = circleViewModel.height.value
                        val currentWidth = circleViewModel.width.value

                        // Вычисляем предполагаемые новые значения высоты и ширины
                        val newHeight = currentHeight * scaleChange
                        val newWidth = currentWidth * scaleChange

                        // Флаги для проверки, достигнуты ли пределы высоты или ширины
                        val isHeightMaxed = currentHeight >= circleViewModel.maxH.value
                        val isHeightMinimized = currentHeight <= circleViewModel.minH.value
                        val isWidthMaxed = currentWidth >= circleViewModel.maxW.value
                        val isWidthMinimized = currentWidth <= circleViewModel.minW.value

                        // Обновляем высоту, только если ширина не достигла предела
                        var adjustedHeight = when {
                            isWidthMaxed && scaleChange > 1f -> currentHeight // Если ширина на максимуме, не увеличиваем высоту
                            isWidthMinimized && scaleChange < 1f -> currentHeight // Если ширина на минимуме, не уменьшаем высоту
                            else -> newHeight.coerceIn(circleViewModel.minH.value, circleViewModel.maxH.value)
                        }

                        // Обновляем ширину, только если высота не достигла предела
                        var adjustedWidth = when {
                            isHeightMaxed && scaleChange > 1f -> currentWidth // Если высота на максимуме, не увеличиваем ширину
                            isHeightMinimized && scaleChange < 1f -> currentWidth // Если высота на минимуме, не уменьшаем ширину
                            // Если высота на минимуме, не уменьшаем ширину
                            else -> newWidth.coerceIn(circleViewModel.minW.value, circleViewModel.maxW.value)
                        }

                        // Если высота и ширина равны, устанавливаем максимальные/минимальные значения
                        if (newHeight == newWidth) {
                            adjustedWidth = adjustedHeight.coerceIn(
                                max(circleViewModel.minW.value, circleViewModel.minH.value),
                                min(circleViewModel.maxW.value, circleViewModel.maxH.value)
                            )
                            adjustedHeight = adjustedHeight.coerceIn(
                                max(circleViewModel.minW.value, circleViewModel.minH.value),
                                min(circleViewModel.maxW.value, circleViewModel.maxH.value)
                            )
                        }

                        // Обновляем значения в ViewModel
                        circleViewModel.height.value = adjustedHeight
                        circleViewModel.width.value = adjustedWidth

                        // Обновляем поворот
                        circleViewModel.rotation.value += rotateChange

                        // Рассчитываем косинус и синус угла вращения в радианах
                        val rotationRadians =
                            Math.toRadians(circleViewModel.rotation.value.toDouble())
                        val cosRotation = cos(rotationRadians).toFloat()
                        val sinRotation = sin(rotationRadians).toFloat()

                        // MinMax значения для смещения
                        val minOffsetX = -circleViewModel.left.value
                        val minOffsetY = -circleViewModel.top.value
                        val maxOffsetX = maxWidth - circleViewModel.right.value
                        val maxOffsetY = maxHeight - circleViewModel.bottom.value

                        var rotatedOffsetY = offsetChange.y
                        var rotatedOffsetX = offsetChange.x

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
                        circleViewModel.offset.value = Offset(
                            x = circleViewModel.offset.value.x + transformedOffsetX,
                            y = circleViewModel.offset.value.y + transformedOffsetY
                        )
                    }}
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { offset ->
                            val rotationRadians = Math.toRadians(circleViewModel.rotation.value.toDouble())
                            val cosRotation = cos(rotationRadians).toFloat()
                            val sinRotation = sin(rotationRadians).toFloat()

                            // Рассчет локального оффсета на фигуре
                            var x = with(localDensity) { offset.x.toDp().value}
                            val y = with(localDensity) { offset.y.toDp().value}

                            // Применение вращения к смещению
                            var transformedOffsetX = x * cosRotation - y * sinRotation
                            var transformedOffsetY = x * sinRotation + y * cosRotation

                            touchOffset.value = Offset(
                                (transformedOffsetX + circleViewModel.circleOffsetInWindow.value.x) ,
                                (transformedOffsetY + circleViewModel.circleOffsetInWindow.value.y),
                            )
                            // Показываем контекстное меню
                            circleViewModel.showContextMenu.value = true
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

                    if(circleViewModel.isInitialUserSize.value) {
                        var offsetX = circleViewModel.offset.value.x
                        var offsetY = circleViewModel.offset.value.y

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
                        circleViewModel.offset.value = Offset(offsetX, offsetY)
                    }

                    // Обновление границ
                    val newBounds = layoutCoordinates.boundsInParent()
                    circleViewModel.left.value = with(localDensity) { newBounds.left.toDp().value }
                    circleViewModel.top.value = with(localDensity) { newBounds.top.toDp().value }
                    circleViewModel.right.value = with(localDensity) { newBounds.right.toDp().value }
                    circleViewModel.bottom.value = with(localDensity) { newBounds.bottom.toDp().value }

                    // Обновление позиции в окне
                    val windowPos = layoutCoordinates.positionInWindow()
                    circleViewModel.circleOffsetInWindow.value = with(localDensity) { Offset(windowPos.x.toDp().value, windowPos.y.toDp().value) }
                },
            verticalAlignment = Alignment.valueOf(circleViewModel.textVerticalAlignment)
        ){
            // Перебираем текстовые блоки внутри фигуры
            circleViewModel.text?.forEach { textBlock ->
                // Создаем строку с аннотациями для стилизации текста
                val annotatedString = buildAnnotatedString {
                    textBlock.text.forEach { textSegment ->
                        // Применяем стили к каждому сегменту текста
                        withStyle(
                            style = SpanStyle(
                                color = Color(android.graphics.Color.parseColor(textSegment.fontColor)),
                                fontSize = textSegment.fontSize.sp,
                                fontWeight = if (textSegment.type == "bold") FontWeight.Bold else FontWeight.Normal,
                                textDecoration = if (textSegment.textDecoration == "underline") TextDecoration.Underline else null
                            ),
                        ) {
                            append(textSegment.text)
                        }
                    }
                }
                // Отрисовываем текст внутри Row с применением заданных стилей и выравниванием
                Text(
                    text = annotatedString,
                    textAlign = TextAlign.valueOf(textBlock.alignment),
                )
            }
        }

        if (circleViewModel.showContextMenu.value) {
            ContextMenu(
                circleViewModel.showContextMenu,
                circleViewModel.showResizeDialog,
                circleViewModel.showDeleteDialog,
                circleViewModel.showChangeBackgroundColorDialog,
                circleViewModel.showChangeBorderSettingDialog,
                touchOffset.value,
            )
        }

        if (circleViewModel.showDeleteDialog.value) {
            DeleteDialog(circleViewModel.showDeleteDialog){
                circleViewModel.deleteShape()
            }
        }

        if (circleViewModel.showResizeDialog.value) {
            ResizeDialog(
                circleViewModel.showResizeDialog,
                circleViewModel.width,
                circleViewModel.height,
                circleViewModel.zIndex,
                circleViewModel.minW,
                circleViewModel.maxW,
                circleViewModel.minH,
                circleViewModel.maxH,
                circleViewModel.isInitialUserSize
            )
        }

        if (circleViewModel.showChangeBackgroundColorDialog.value) {
            ChangeColorDialog(
                circleViewModel.showChangeBackgroundColorDialog,
                circleViewModel.color,
                CHANGE_BACKGROUND_COLOR
            )
        }

        if (circleViewModel.showChangeBorderSettingDialog.value) {
            ChangeSettingBorderDialog(
                circleViewModel.showChangeBorderSettingDialog,
                circleViewModel.showChangeBorderColorDialog,
                circleViewModel.borderColor,
                circleViewModel.borderWidth,
                circleViewModel.cornerRadius
            )
        }

        if (circleViewModel.showChangeBorderColorDialog.value) {
            ChangeColorDialog(
                circleViewModel.showChangeBorderColorDialog,
                circleViewModel.borderColor,
                CHANGE_BORDER_COLOR
            )
        }

    }
}