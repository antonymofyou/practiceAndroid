package com.example.practiceandroid.views.shapes

import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.practiceandroid.viewModels.shapesmodel.ArrowViewModel
import com.example.practiceandroid.views.contextmenu.border.ChangeSettingBorderDialog
import com.example.practiceandroid.views.contextmenu.color.ChangeColorDialog
import com.example.practiceandroid.views.contextmenu.ContextMenu
import com.example.practiceandroid.views.contextmenu.DeleteDialog
import com.example.practiceandroid.views.contextmenu.ResizeDialog
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.graphics.Path

const val WIDTH_CORRECTION_FACTOR = 2f

// Компонент для отрисовки стрелки на основе данных, переданных в объекте shape
@Composable
fun DrawArrow(arrowViewModel: ArrowViewModel, focusManager: FocusManager, maxWidth: Float, maxHeight: Float) {
    // Преобразуем ширину и высоту стрелки в тип Float для дальнейшего использования
    var widthFloat  = arrowViewModel.width.value.value
    var heightFloat = arrowViewModel.height.value.value

    // Дополнительная высота наконечника стрелки, которая добавляется к общей высоте стрелки
    val arrowHeadHeightExtra = 50
    // Коэффициент, на который будет увеличена ширина стрелки для создания наконечника
    val arrowHeadWidthExtra = 1.25f

    // Вспомогательная переменная для рекомпозиции
    var recomposition = 0f

    // Отслеживание позиции нажатия
    val touchOffset = remember { mutableStateOf(Offset.Zero) }

    val localDensity = LocalDensity.current

    if (arrowViewModel.visibility.value) {
        Canvas(
            modifier = Modifier
                .graphicsLayer(
                    rotationZ = arrowViewModel.rotation.value,
                    translationX = arrowViewModel.offset.value.x,
                    translationY = arrowViewModel.offset.value.y,
                )
                .zIndex(arrowViewModel.zIndex.value)
                .clickable { focusManager.clearFocus() }
                .pointerInput(Unit) {
                    detectTransformGestures { _, offsetChange, scaleChange, rotateChange ->
                        arrowViewModel.isInitialUserSize.value = false

                        // Обновляем масштаб в ViewModel
                        arrowViewModel.height.value = (arrowViewModel.height.value * scaleChange)
                            .coerceIn(arrowViewModel.minH.value, arrowViewModel.maxH.value)
                        arrowViewModel.width.value = (arrowViewModel.width.value * scaleChange)
                            .coerceIn(arrowViewModel.minW.value, arrowViewModel.maxW.value)

                        // Обновляем поворот
                        arrowViewModel.rotation.value += rotateChange

                        // Рассчитываем косинус и синус угла вращения в радианах
                        val rotationRadians =
                            Math.toRadians(arrowViewModel.rotation.value.toDouble())
                        val cosRotation = cos(rotationRadians).toFloat()
                        val sinRotation = sin(rotationRadians).toFloat()

                        // MinMax значения для смещения
                        val minOffsetX = -arrowViewModel.left.value
                        val minOffsetY = -arrowViewModel.top.value
                        val maxOffsetX = maxWidth - arrowViewModel.right.value
                        val maxOffsetY = maxHeight - arrowViewModel.bottom.value

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
                        arrowViewModel.offset.value = Offset(
                            x = arrowViewModel.offset.value.x + transformedOffsetX,
                            y = arrowViewModel.offset.value.y + transformedOffsetY
                        )
                    }}
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { offset ->
                            val rotationRadians = Math.toRadians(arrowViewModel.rotation.value.toDouble())
                            val cosRotation = cos(rotationRadians).toFloat()
                            val sinRotation = sin(rotationRadians).toFloat()

                            // Рассчет локального оффсета на фигуре
                            var x = with(localDensity) { offset.x.toDp().value}
                            val y = with(localDensity) { offset.y.toDp().value}

                            // Применение вращения к смещению
                            var transformedOffsetX = x * cosRotation - y * sinRotation
                            var transformedOffsetY = x * sinRotation + y * cosRotation

                            touchOffset.value = Offset(
                                (transformedOffsetX +  arrowViewModel.arrowOffsetInWindow.value.x) ,
                                (transformedOffsetY +  arrowViewModel.arrowOffsetInWindow.value.y),
                            )
                            // Показываем контекстное меню
                            arrowViewModel.showContextMenu.value = true
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

                    if(arrowViewModel.isInitialUserSize.value) {
                        var offsetX = arrowViewModel.offset.value.x
                        var offsetY = arrowViewModel.offset.value.y

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
                        arrowViewModel.offset.value = Offset(offsetX, offsetY)
                    }

                    // Обновление границ
                    val newBounds = layoutCoordinates.boundsInParent()
                    arrowViewModel.left.value = with(localDensity) { newBounds.left.toDp().value }
                    arrowViewModel.top.value = with(localDensity) { newBounds.top.toDp().value }
                    arrowViewModel.right.value = with(localDensity) { newBounds.right.toDp().value }
                    arrowViewModel.bottom.value = with(localDensity) { newBounds.bottom.toDp().value }

                    // Обновление позиции в окне
                    val windowPos = layoutCoordinates.positionInWindow()
                    arrowViewModel.arrowOffsetInWindow.value = with(localDensity) { Offset(windowPos.x.toDp().value, windowPos.y.toDp().value) }

                }
                .requiredHeight(with(localDensity) {arrowViewModel.height.value.value.toDp()})
                .requiredWidth(with(localDensity) {arrowViewModel.width.value.value.toDp()})
        ) {

            val cornerRadius = arrowViewModel.cornerRadius.value.value

            val arrowPath = Path().apply {
                // Начинаем рисовать путь с верхнего левого угла с учетом дополнительной высоты для вершины стрелки
                moveTo(0f + arrowHeadHeightExtra, 0f)

                // Рисуем верхнюю горизонтальную линию от левого угла до правого с учетом радиуса скругления
                lineTo(widthFloat - cornerRadius, 0f)

                // Скругление верхнего правого угла, используя квадратичную кривую Безье
                quadraticBezierTo(widthFloat, 0f, widthFloat, 0f - cornerRadius)

                // Линия к месту, где начинается вершина стрелки
                lineTo(widthFloat, 0f - arrowHeadHeightExtra)

                // Линия от правого верхнего края к середине фигуры, создавая вершину стрелки
                lineTo(widthFloat * arrowHeadWidthExtra - (cornerRadius / 1.25f / 2f), heightFloat / 2f - cornerRadius)

                // Скругление верхней части стрелки, используя квадратичную кривую Безье
                quadraticBezierTo(
                    widthFloat * arrowHeadWidthExtra,  // Контрольная точка кривой
                    heightFloat / 2f,                  // Контрольная точка по вертикали
                    widthFloat * arrowHeadWidthExtra - (cornerRadius / 1.25f / 2f),  // Точка завершения
                    heightFloat / 2f + cornerRadius    // Точка завершения по вертикали
                )

                // Рисуем линию вниз от середины к нижнему правому углу с учетом высоты стрелки
                lineTo(widthFloat, heightFloat + arrowHeadHeightExtra)

                // Линия к нижнему краю
                lineTo(widthFloat, heightFloat + cornerRadius)

                // Скругление нижнего правого угла
                quadraticBezierTo(widthFloat, heightFloat, widthFloat - cornerRadius, heightFloat)

                // Рисуем линию влево к нижнему левому углу
                lineTo(0f + cornerRadius, heightFloat)

                // Скругление нижнего левого угла
                quadraticBezierTo(0f, heightFloat, 0f, heightFloat - cornerRadius)

                // Линия вверх вдоль левого края
                lineTo(0f, 0f + cornerRadius)

                // Скругление верхнего левого угла
                quadraticBezierTo(0f, 0f, 0f + cornerRadius, 0f)

                // Замыкаем путь, возвращаясь к начальной точке
                close()
            }

            // Нарисовать границу
            drawPath(
                path = arrowPath,
                color = Color.Blue,//Color(android.graphics.Color.parseColor(arrowViewModel.borderColor.value)),
                style = Stroke(width = arrowViewModel.borderWidth.value.toPx()  * WIDTH_CORRECTION_FACTOR)
            )

            // Нарисовать заливку
            drawPath(
                path = arrowPath,
                color = Color(android.graphics.Color.parseColor(arrowViewModel.fill.value)),
            )
        }
    }

    if (arrowViewModel.showContextMenu.value) {
        ContextMenu(
            arrowViewModel.showContextMenu,
            arrowViewModel.showResizeDialog,
            arrowViewModel.showDeleteDialog,
            arrowViewModel.showChangeBackgroundColorDialog,
            arrowViewModel.showChangeBorderSettingDialog,
            touchOffset.value,
        )
    }

    if (arrowViewModel.showDeleteDialog.value) {
        DeleteDialog(arrowViewModel.showDeleteDialog){
            arrowViewModel.deleteShape()
        }
    }

    if (arrowViewModel.showResizeDialog.value) {
        ResizeDialog(
            arrowViewModel.showResizeDialog,
            arrowViewModel.width,
            arrowViewModel.height,
            arrowViewModel.zIndex,
            arrowViewModel.minW,
            arrowViewModel.maxW,
            arrowViewModel.minH,
            arrowViewModel.maxH,
            arrowViewModel.isInitialUserSize
        )
    }

    if (arrowViewModel.showChangeBackgroundColorDialog.value) {
        ChangeColorDialog(
            arrowViewModel.showChangeBackgroundColorDialog,
            arrowViewModel.fill,
            CHANGE_BACKGROUND_COLOR
        )
    }

    if (arrowViewModel.showChangeBorderSettingDialog.value) {
        ChangeSettingBorderDialog(
            arrowViewModel.showChangeBorderSettingDialog,
            arrowViewModel.showChangeBorderColorDialog,
            arrowViewModel.borderColor,
            arrowViewModel.borderWidth,
            arrowViewModel.cornerRadius
        )
    }

    if (arrowViewModel.showChangeBorderColorDialog.value) {
        ChangeColorDialog(
            arrowViewModel.showChangeBorderColorDialog,
            arrowViewModel.borderColor,
            CHANGE_BORDER_COLOR
        )
    }
}