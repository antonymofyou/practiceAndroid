package com.example.practiceandroid.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.practiceandroid.ext.valueOf
import com.example.practiceandroid.models.ResponseShapes
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

// Компонент для отрисовки прямоугольника на основе данных, переданных в объекте shape
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawRectangle(
    shape: ResponseShapes.Shape,
    focusManager: FocusManager,
    maxWidth: Float,
    maxHeight: Float,
) {
    var scale by remember { mutableStateOf(1f) }

    var offset by remember { mutableStateOf(Offset(shape.x, shape.y)) }

    // Границы элемента
    var top = offset.x
    var left = offset.y
    var right = left + shape.width.dp.value
    var bottom = top + shape.height.dp.value

    val localDensity = LocalDensity.current
    val state = rememberTransformableState { scaleChange, offsetChange, _ ->
        // Обновляем масштаб с ограничением в пределах от 0.85f до 3f
        scale = (scale * scaleChange).coerceIn(0.85f, 3f)

        // Рассчитываем косинус и синус угла вращения в радианах
        val rotationRadians = Math.toRadians((shape.rotation?.toDouble() ?: 0.0))
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

    }

    // Контейнер Row для размещения текста внутри прямоугольника
    Row(
        modifier = Modifier
            .graphicsLayer(
                rotationZ = shape.rotation ?: 0f,
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y,
            )
            // Устанавливаем фоновый цвет и закругленные углы для прямоугольника
            .background(
                color = Color(android.graphics.Color.parseColor(shape.color)),
                shape = RoundedCornerShape(shape.cornerRadius?.dp ?: 0.dp)
            )
            // Устанавливаем границу для прямоугольника
            .border(
                width = shape.borderWidth?.dp ?: 0.dp,
                color = Color(android.graphics.Color.parseColor(shape.borderColor)),
                shape = RoundedCornerShape(shape.cornerRadius?.dp ?: 0.dp)
            )
            .width(shape.width.dp)
            .height(shape.height.dp)
            .zIndex(shape.zIndex)
            .clickable { focusManager.clearFocus() }
            .onGloballyPositioned { layoutCoordinates  ->
                val rect = layoutCoordinates.boundsInParent()
                left = with(localDensity) { rect.left.toDp().value }
                top = with(localDensity) { rect.top.toDp().value }
                right = with(localDensity) { rect.right.toDp().value }
                bottom = with(localDensity) { rect.bottom.toDp().value }
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, offsetChange, scaleChange, _ ->

                    // Обновляем масштаб с ограничением в пределах от 0.85f до 3f
                    scale = (scale * scaleChange).coerceIn(0.85f, 3f)

                    // Рассчитываем косинус и синус угла вращения в радианах
                    val rotationRadians = Math.toRadians((shape.rotation?.toDouble() ?: 0.0))
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

                }},
        verticalAlignment = Alignment.valueOf(shape.textVerticalAlignment)
    ) {
        // Перебираем текстовые блоки внутри фигуры
        shape.text?.forEach { textBlock ->
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
}
