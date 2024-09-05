package com.example.practiceandroid.views

import MainViewModel
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practiceandroid.ext.valueOf
import com.example.practiceandroid.models.ResponseShapes
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

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(rectangleViewModel.offset) }
    var rotate by remember { mutableStateOf(rectangleViewModel.rotation)}

    // Границы элемента
    var top by remember { mutableFloatStateOf(0f) }
    var left by remember { mutableFloatStateOf(0f) }
    var right by remember { mutableFloatStateOf(0f) }
    var bottom by remember { mutableFloatStateOf(0f) }

    var showContextMenu = remember { mutableStateOf(false) }// Контекстное меню
    var showDeleteDialog = remember  { mutableStateOf(false) }// Окно подтверждения удаления
    var showResizeDialog = remember  { mutableStateOf(false) }// Окно изменения размеров

    // Вспомогательная переменная для рекомпозиции
    var recomposition = 0f

    val localDensity = LocalDensity.current

    // Контейнер Row для размещения текста внутри прямоугольника
    Row(
        modifier = Modifier
            .graphicsLayer(
                rotationZ = rotate,
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y,
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
                    scale = (scale * scaleChange).coerceIn(0.85f, 3f)

                    // Обновляем поворот
                    rotate += rotateChange

                    // Рассчитываем косинус и синус угла вращения в радианах
                    val rotationRadians = Math.toRadians(rotate.toDouble())
                    val cosRotation = cos(rotationRadians).toFloat()
                    val sinRotation = sin(rotationRadians).toFloat()

                    // MinMax значения для смещения
                    val minOffsetX = -left
                    val minOffsetY = -top
                    val maxOffsetX = maxWidth - right
                    val maxOffsetY = maxHeight - bottom

                    var rotatedOffsetY = offsetChange.y * scale
                    var rotatedOffsetX = offsetChange.x * scale

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
                    offset = Offset(
                        x = offset.x + transformedOffsetX,
                        y = offset.y + transformedOffsetY
                    )
                }}
            .pointerInput(Unit){
                detectTapGestures(
                    onLongPress = {
                        showContextMenu.value = true
                    }
                )
            }
            .onGloballyPositioned { layoutCoordinates  ->
                recomposition
                val rect = layoutCoordinates.boundsInParent()
                left = with(localDensity) { rect.left.toDp().value }
                top = with(localDensity) { rect.top.toDp().value }
                right = with(localDensity) { rect.right.toDp().value }
                bottom = with(localDensity) { rect.bottom.toDp().value }
            },
        verticalAlignment = Alignment.valueOf(rectangleViewModel.textVerticalAlignment)
    ) {
        // Перебираем текстовые блоки внутри фигуры
        rectangleViewModel.text?.forEach { textBlock ->
            textBlock.text.forEachIndexed { index, textSegment ->
                val text = remember{mutableStateOf( textSegment.text)}
                BasicTextField(
                    value = text.value,
                    {text.value = it},
                    textStyle = TextStyle(textAlign = TextAlign.valueOf(textBlock.alignment),
                        color = Color(android.graphics.Color.parseColor(textSegment.fontColor)),
                        fontSize = textSegment.fontSize.sp,
                        fontWeight = if (textSegment.type == "bold") FontWeight.Bold else FontWeight.Normal,
                        textDecoration = if (textSegment.textDecoration == "underline") TextDecoration.Underline else null),
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


    if (showContextMenu.value) {
        ContextMenu(showContextMenu, showResizeDialog, showDeleteDialog )
    }

    if (showDeleteDialog.value) {
        DeleteDialog(showDeleteDialog)
    }

    if (showResizeDialog.value) {
        ResizeDialog(showResizeDialog, rectangleViewModel.width, rectangleViewModel.height, rectangleViewModel.zIndex)
    }
}

@Composable
fun ResizeDialog(
    showResizeDialog: MutableState<Boolean>,
    width: MutableState<Dp>,
    height: MutableState<Dp>,
    zIndex: MutableState<Float>
) {
    AlertDialog(
        onDismissRequest = { showResizeDialog.value = false },
        confirmButton = {
            TextButton(onClick = {
                //onUpdateShape(newWidth, newHeight, newZIndex)
                showResizeDialog.value = false
            }) {
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
                    value = width.value.toString(),
                    onValueChange = { width.value = it.toFloat().dp},
                    label = { Text("Ширина") }
                )
                OutlinedTextField(
                    value = height.value.toString(),
                    onValueChange = { height.value = it.toFloat().dp},
                    label = { Text("Высота") }
                )
                OutlinedTextField(
                    value = zIndex.value.toString(),
                    onValueChange = { zIndex.value = it.toFloat()},
                    label = { Text("Слой (zIndex)") }
                )
            }
        }
    )
}


@Composable
fun DeleteDialog(showDeleteDialog: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = { showDeleteDialog.value = false },
        confirmButton = {
            TextButton(onClick = {
                //onDelete()
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


@Composable
fun ContextMenu(showContextMenu: MutableState<Boolean>,
                showResizeDialog: MutableState<Boolean>,
                showDeleteDialog: MutableState<Boolean>
                ) {
    DropdownMenu(
        expanded = showContextMenu.value,
        onDismissRequest = { showContextMenu.value = false }
    ) {
        DropdownMenuItem(
            text = { Text("Удалить") },
            onClick = {
                showDeleteDialog.value = true
                showContextMenu.value = false
            }
        )
        DropdownMenuItem(
            text = { Text("Изменить размер") },
            onClick = {
                showResizeDialog.value = true
                showContextMenu.value = false
            }
        )
    }
}

