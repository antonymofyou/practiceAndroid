package com.example.practiceandroid.views.shapes

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
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
    // Границы элемента
    var top by remember { mutableFloatStateOf(0f) }
    var left by remember { mutableFloatStateOf(0f) }
    var right by remember { mutableFloatStateOf(0f) }
    var bottom by remember { mutableFloatStateOf(0f) }

    // Глобальный оффсет элемента
    var imageOffsetInWindow = remember { mutableStateOf(Offset.Zero) }

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
                    scaleX = imageViewModel.scale.value,
                    scaleY = imageViewModel.scale.value,
                    translationX = imageViewModel.offset.value.x,
                    translationY = imageViewModel.offset.value.y,
                )
                // Устанавливаем границу для прямоугольника
                .border(
                    width = imageViewModel.borderWidth.value,
                    color = Color(android.graphics.Color.parseColor(imageViewModel.borderColor.value)),
                    shape = RoundedCornerShape(imageViewModel.cornerRadius.value)
                )
                .width(imageViewModel.width.value)
                .height(imageViewModel.height.value)
                .zIndex(imageViewModel.zIndex.value)
                .clip(shape = RoundedCornerShape(imageViewModel.cornerRadius.value))
                .clickable { focusManager.clearFocus() }
                .pointerInput(Unit) {
                    detectTransformGestures { _, offsetChange, scaleChange, rotateChange ->
                        // Обновляем масштаб с ограничением в пределах от 0.85f до 3f
                        imageViewModel.scale.value =
                            (imageViewModel.scale.value * scaleChange).coerceIn(0.85f, 3f)

                        // Обновляем поворот
                        imageViewModel.rotation.value += rotateChange

                        // Рассчитываем косинус и синус угла вращения в радианах
                        val rotationRadians =
                            Math.toRadians(imageViewModel.rotation.value.toDouble())
                        val cosRotation = cos(rotationRadians).toFloat()
                        val sinRotation = sin(rotationRadians).toFloat()

                        // MinMax значения для смещения
                        val minOffsetX = -left
                        val minOffsetY = -top
                        val maxOffsetX = maxWidth - right
                        val maxOffsetY = maxHeight - bottom

                        var rotatedOffsetY = offsetChange.y * imageViewModel.scale.value
                        var rotatedOffsetX = offsetChange.x * imageViewModel.scale.value

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
                                with(localDensity) { offset.x.toDp().value } * imageViewModel.scale.value
                            val y =
                                with(localDensity) { offset.y.toDp().value } * imageViewModel.scale.value

                            // Применение вращения к смещению
                            var transformedOffsetX = x * cosRotation - y * sinRotation
                            var transformedOffsetY = x * sinRotation + y * cosRotation

                            touchOffset.value = Offset(
                                (transformedOffsetX + imageOffsetInWindow.value.x),
                                (transformedOffsetY + imageOffsetInWindow.value.y),
                            )
                            // Показываем контекстное меню
                            imageViewModel.showContextMenu.value = true
                        }
                    )
                }
                .onGloballyPositioned { layoutCoordinates ->
                    recomposition
                    val rect = layoutCoordinates.boundsInParent()
                    left = with(localDensity) { rect.left.toDp().value }
                    top = with(localDensity) { rect.top.toDp().value }
                    right = with(localDensity) { rect.right.toDp().value }
                    bottom = with(localDensity) { rect.bottom.toDp().value }
                }
        )

        if (imageViewModel.showContextMenu.value) {
            ContextMenu(
                imageViewModel.showContextMenu,
                imageViewModel.showResizeDialog,
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

        if (imageViewModel.showResizeDialog.value) {
            ResizeDialog(
                imageViewModel.showResizeDialog,
                imageViewModel.width,
                imageViewModel.height,
                imageViewModel.zIndex,
                imageViewModel.scale,
            )
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