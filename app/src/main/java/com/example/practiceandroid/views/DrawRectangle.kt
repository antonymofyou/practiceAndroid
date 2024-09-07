package com.example.practiceandroid.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.practiceandroid.ext.valueOf
import com.example.practiceandroid.viewModels.RectangleViewModel
import kotlin.math.cos
import kotlin.math.sin

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
    var left by remember { mutableFloatStateOf(0f) }
    var right by remember { mutableFloatStateOf(0f) }
    var bottom by remember { mutableFloatStateOf(0f) }

    // Глобальный оффсет элемента
    var rectangleOffsetInWindow = remember { mutableStateOf(Offset.Zero) }

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
                    scaleX = rectangleViewModel.scale.value,
                    scaleY = rectangleViewModel.scale.value,
                    translationX = rectangleViewModel.offset.value.x,
                    translationY = rectangleViewModel.offset.value.y,
                )
                // Устанавливаем фоновый цвет и закругленные углы для прямоугольника
                .background(
                    color = Color(android.graphics.Color.parseColor(rectangleViewModel.color)),
                    shape = RoundedCornerShape(rectangleViewModel.cornerRadius)
                )
                // Устанавливаем границу для прямоугольника
                .border(
                    width = rectangleViewModel.borderWidth,
                    color = Color(android.graphics.Color.parseColor(rectangleViewModel.color)),
                    shape = RoundedCornerShape(rectangleViewModel.cornerRadius)
                )
                .width(rectangleViewModel.width.value)
                .height(rectangleViewModel.height.value)
                .zIndex(rectangleViewModel.zIndex.value)
                .clickable { focusManager.clearFocus() }
                .pointerInput(Unit) {
                    detectTransformGestures { _, offsetChange, scaleChange, rotateChange ->
                        // Обновляем масштаб с ограничением в пределах от 0.85f до 3f
                        rectangleViewModel.scale.value =
                            (rectangleViewModel.scale.value * scaleChange).coerceIn(0.85f, 3f)

                        // Обновляем поворот
                        rectangleViewModel.rotation.value += rotateChange

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
                            val rotationRadians = Math.toRadians(rectangleViewModel.rotation.value.toDouble())
                            val cosRotation = cos(rotationRadians).toFloat()
                            val sinRotation = sin(rotationRadians).toFloat()

                            // Рассчет локального оффсета на фигуре
                            var x = with(localDensity) { offset.x.toDp().value} * rectangleViewModel.scale.value
                            val y = with(localDensity) { offset.y.toDp().value} * rectangleViewModel.scale.value

                            // Применение вращения к смещению
                            var transformedOffsetX = x * cosRotation - y * sinRotation
                            var transformedOffsetY = x * sinRotation + y * cosRotation

                            touchOffset.value = Offset(
                                (transformedOffsetX +  rectangleOffsetInWindow.value.x) ,
                                (transformedOffsetY +  rectangleOffsetInWindow.value.y),
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

                    val rect2 = layoutCoordinates.positionInWindow()
                    val x = with(localDensity) { rect2.x.toDp().value}
                    val y = with(localDensity) { rect2.y.toDp().value}
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

    if (rectangleViewModel.showContextMenu.value) {
        ContextMenu(
            rectangleViewModel.showContextMenu,
            rectangleViewModel.showResizeDialog,
            rectangleViewModel.showDeleteDialog,
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
        ) { newWidth, newHeight, newZIndex ->
            rectangleViewModel.updateShape(newWidth, newHeight, newZIndex)
        }
    }
}

@Composable
fun ContextMenu(
    showContextMenu: MutableState<Boolean>,
    showResizeDialog: MutableState<Boolean>,
    showDeleteDialog: MutableState<Boolean>,
    offset: Offset,
) {
        DropdownMenu(
            expanded = showContextMenu.value,
            onDismissRequest = { showContextMenu.value = false },
            offset = DpOffset(offset.x.dp, offset.y.dp)
        ) {
            DropdownMenuItem(
                text = { Text("Удалить") },
                onClick = {
                    showDeleteDialog.value = true
                    showContextMenu.value = false
                },
                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = "Удалить") }
            )
            DropdownMenuItem(
                text = { Text("Изменить размер") },
                onClick = {
                    showResizeDialog.value = true
                    showContextMenu.value = false
                },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "Удалить") }
            )
            DropdownMenuItem(
                text = { Text("Изменить фон") },
                onClick = {
                    showResizeDialog.value = true
                    showContextMenu.value = false
                },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "Удалить") }
            )
        }

}

@Composable
fun ResizeDialog(
    showResizeDialog: MutableState<Boolean>,
    width: MutableState<Dp>,
    height: MutableState<Dp>,
    zIndex: MutableState<Float>,
    scale: MutableState<Float>,
    onUpdateShape: (Dp, Dp, Float) -> Unit
) {
    var newWidth by remember { mutableStateOf((width.value.value * scale.value).toString()) }
    var newHeight by remember { mutableStateOf((height.value.value * scale.value).toString()) }
    var newZIndex by remember { mutableStateOf((zIndex.value * scale.value).toString()) }

    val isConfirmButtonEnabled = newWidth.isNotEmpty() && newHeight.isNotEmpty() && newZIndex.isNotEmpty()

    AlertDialog(
        onDismissRequest = {
            showResizeDialog.value = false },
        confirmButton = {
            TextButton(onClick = {
                onUpdateShape(newWidth.toFloat().dp, newHeight.toFloat().dp, newZIndex.toFloat())
                showResizeDialog.value = false
            },
                enabled = isConfirmButtonEnabled) {
                Text("ОК")
            }
        },
        dismissButton = {
            TextButton(onClick = { showResizeDialog.value = false }) {
                Text("Отмена")
            }
        },
        title = { Text("Изменить размеры и слой") },
        text = {
            Column {
                OutlinedTextField(
                    value = newWidth,
                    onValueChange = {
                        val filteredText = it.filter { it.isDigit() || it == '.'}
                        newWidth = filteredText},
                    label = { Text("Ширина") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                OutlinedTextField(
                    value = newHeight,
                    onValueChange = {
                        val filteredText = it.filter { it.isDigit() || it == '.'}
                        newHeight = filteredText },
                    label = { Text("Высота") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = newZIndex,
                    onValueChange = { newZIndex = it},
                    label = { Text("Слой (zIndex)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    )
}


@Composable
fun DeleteDialog(
    showDeleteDialog: MutableState<Boolean>,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { showDeleteDialog.value = false },
        confirmButton = {
            TextButton(onClick = {
                onDelete()
                showDeleteDialog.value = false
            }) {
                Text("Да")
            }
        },
        dismissButton = {
            TextButton(onClick = { showDeleteDialog.value = false }) {
                Text("Нет")
            }
        },
        title = { Text("Подтверждение удаления") },
        text = { Text("Вы уверены, что хотите удалить эту фигуру?") }
    )
}



