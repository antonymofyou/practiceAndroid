package com.example.practiceandroid.views.shapes

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.zIndex
import com.example.practiceandroid.viewModels.shapesmodel.ImageViewModel
import com.example.practiceandroid.views.contextmenu.border.ChangeSettingBorderDialog
import com.example.practiceandroid.views.contextmenu.color.ChangeColorDialog
import com.example.practiceandroid.views.contextmenu.ContextMenu
import com.example.practiceandroid.views.contextmenu.DeleteDialog
import com.example.practiceandroid.views.contextmenu.ResizeDialog
import kotlin.math.cos
import kotlin.math.sin

/**
    Компонент для отрисовки изображения, переданного через image и отрисовка с учетом
    параметров из shape
 */
@Composable
fun DrawImage(
    imageViewModel: ImageViewModel,
    focusManager: FocusManager,
    maxWidth: Float,
    maxHeight: Float
) {
    // Вспомогательная переменная для рекомпозиции
    var recomposition = 0f

    // Отслеживание позиции нажатия
    val touchOffset = remember { mutableStateOf(Offset.Zero) }

    val localDensity = LocalDensity.current

    if (imageViewModel.visibility.value) {
        Image(
            bitmap = imageViewModel.imageBitmap,
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer(
                    rotationZ = imageViewModel.rotation.value,
                    translationX = imageViewModel.offset.value.x,
                    translationY = imageViewModel.offset.value.y,
                )
                // Устанавливаем границу для прямоугольника
                .border(
                    width = imageViewModel.borderWidth.value,
                    color = Color(android.graphics.Color.parseColor(imageViewModel.borderColor.value)),
                    shape = RoundedCornerShape(imageViewModel.cornerRadius.value)
                )
                .requiredWidth(imageViewModel.width.value)
                .requiredHeight(imageViewModel.height.value)
                .zIndex(imageViewModel.zIndex.value)
                .clip(shape = RoundedCornerShape(imageViewModel.cornerRadius.value))
                .clickable { focusManager.clearFocus() }
                .pointerInput(Unit) {
                    detectTransformGestures { _, offsetChange, _, rotateChange ->
                        // Обновляем поворот
                        imageViewModel.rotation.value += rotateChange

                        // Рассчитываем косинус и синус угла вращения в радианах
                        val rotationRadians =
                            Math.toRadians(imageViewModel.rotation.value.toDouble())
                        val cosRotation = cos(rotationRadians).toFloat()
                        val sinRotation = sin(rotationRadians).toFloat()

                        // MinMax значения для смещения
                        val minOffsetX = -imageViewModel.left.value
                        val minOffsetY = -imageViewModel.top.value
                        val maxOffsetX = maxWidth - imageViewModel.right.value
                        val maxOffsetY = maxHeight - imageViewModel.bottom.value

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
                        imageViewModel.offset.value = Offset(
                            x = imageViewModel.offset.value.x + transformedOffsetX,
                            y = imageViewModel.offset.value.y + transformedOffsetY
                        )
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { offset ->
                            val rotationRadians =
                                Math.toRadians(imageViewModel.rotation.value.toDouble())
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
                                (transformedOffsetX + imageViewModel.imageOffsetInWindow.value.x),
                                (transformedOffsetY + imageViewModel.imageOffsetInWindow.value.y),
                            )
                            // Показываем контекстное меню
                            imageViewModel.showContextMenu.value = true
                        }
                    )
                }
                .onGloballyPositioned { layoutCoordinates ->
                    recomposition

                    // Обновление границ
                    val newBounds = layoutCoordinates.boundsInParent()
                    imageViewModel.left.value = with(localDensity) { newBounds.left.toDp().value }
                    imageViewModel.top.value = with(localDensity) { newBounds.top.toDp().value }
                    imageViewModel.right.value = with(localDensity) { newBounds.right.toDp().value }
                    imageViewModel.bottom.value = with(localDensity) { newBounds.bottom.toDp().value }

                    // Обновление позиции в окне
                    val windowPos = layoutCoordinates.positionInWindow()
                    imageViewModel.imageOffsetInWindow.value = with(localDensity) { Offset(windowPos.x.toDp().value, windowPos.y.toDp().value) }

                }
        )

        if (imageViewModel.showContextMenu.value) {
            ContextMenu(
                imageViewModel.showContextMenu,
                imageViewModel.showDeleteDialog,
                imageViewModel.showChangeBorderSettingDialog,
                touchOffset.value,
            )
        }

        if (imageViewModel.showDeleteDialog.value) {
            DeleteDialog(imageViewModel.showDeleteDialog) {
                imageViewModel.deleteShape()
            }
        }

        if (imageViewModel.showChangeBorderSettingDialog.value) {
            ChangeSettingBorderDialog(
                imageViewModel.showChangeBorderSettingDialog,
                imageViewModel.showChangeBorderColorDialog,
                imageViewModel.borderColor,
                imageViewModel.borderWidth,
                imageViewModel.cornerRadius
            )
        }

        if (imageViewModel.showChangeBorderColorDialog.value) {
            ChangeColorDialog(
                imageViewModel.showChangeBorderColorDialog,
                imageViewModel.borderColor,
                CHANGE_BORDER_COLOR
            )
        }
    }
}