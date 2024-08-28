package com.example.practiceandroid.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.practiceandroid.ext.valueOf
import com.example.practiceandroid.models.ResponseShapes
import kotlin.math.cos
import kotlin.math.sin

// Компонент для отрисовки круга на основе данных, переданных в объекте shape
@Composable
fun DrawCircle(shape: ResponseShapes.Shape, focusManager: FocusManager, maxWidth: Float, maxHeight: Float) {

    var scale by remember { mutableStateOf(1f) }

    var offset by remember { mutableStateOf(Offset(shape.x, shape.y)) }

    // Минимально допустимые значения для смещения
    var minOffsetX = -offset.x
    var minOffsetY = -offset.y

    val localDensity = LocalDensity.current
    val state = rememberTransformableState { scaleChange, offsetChange, _ ->
        // Обновляем масштаб с ограничением в пределах от 0.85f до 3f
        scale = (scale * scaleChange).coerceIn(0.85f, 3f)

        // Рассчитываем косинус и синус угла вращения в радианах
        val rotationRadians = Math.toRadians((shape.rotation?.toDouble() ?: 0.0))
        val cosRotation = cos(rotationRadians).toFloat()
        val sinRotation = sin(rotationRadians).toFloat()

        // Рассчитываем минимальные и максимальные смещения с учетом вращения
        val rotatedMinOffsetX = minOffsetX * cosRotation - minOffsetY * sinRotation
        val rotatedMinOffsetY = minOffsetX * sinRotation + minOffsetY * cosRotation

        val scaledWidth = shape.width * scale
        val scaledHeight = shape.height * scale

        val rotatedScaledWidthX = scaledWidth * cosRotation - scaledWidth * sinRotation
        val rotatedScaledHeightY = scaledHeight * sinRotation + scaledHeight * cosRotation

        val maxOffsetX = maxWidth - rotatedScaledWidthX - (-rotatedMinOffsetX)
        val maxOffsetY = maxHeight - rotatedScaledHeightY - (-rotatedMinOffsetY)

        // Ограничение смещения по оси Y
        val rotatedOffsetY = if (minOffsetY > maxOffsetY) {
            (offsetChange.y * scale).coerceIn(maxOffsetY, minOffsetY)
        } else {
            (offsetChange.y * scale).coerceIn(minOffsetY, maxOffsetY)
        }

        // Ограничение смещения по оси X
        val rotatedOffsetX = if (minOffsetX > maxOffsetX) {
            (offsetChange.x * scale).coerceIn(maxOffsetX, minOffsetX)
        } else {
            (offsetChange.x * scale).coerceIn(minOffsetX, maxOffsetX)
        }

        // Применение вращения к смещению
        val transformedOffsetX = rotatedOffsetX * cosRotation - rotatedOffsetY * sinRotation
        val transformedOffsetY = rotatedOffsetX * sinRotation + rotatedOffsetY * cosRotation

        // Обновление значения смещения
        offset = Offset(
            x = offset.x + transformedOffsetX,
            y = offset.y + transformedOffsetY
        )

    }

    Row(
        modifier = Modifier
            .graphicsLayer(
                rotationZ = shape.rotation ?: 0f,
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y,
            )
            .background(
                color = Color(android.graphics.Color.parseColor(shape.color)),
                shape = RoundedCornerShape(shape.cornerRadius?.dp ?: 0.dp)
            )
            .border(
                width = shape.borderWidth?.dp ?: 0.dp,
                color = Color(android.graphics.Color.parseColor(shape.borderColor)),
                shape = RoundedCornerShape(shape.cornerRadius?.dp ?: 0.dp)
            )
            .width(shape.width.dp)
            .height(shape.height.dp)
            .zIndex(shape.zIndex)
            .clickable { focusManager.clearFocus() }
            .onGloballyPositioned { coordinates ->
                val positionInRoot = coordinates.positionInParent()
                minOffsetX = with(localDensity) { -positionInRoot.x.toDp().value }
                minOffsetY = with(localDensity) { -positionInRoot.y.toDp().value }
            }
            .transformable(state),
        verticalAlignment = Alignment.valueOf(shape.textVerticalAlignment)
    ){
        // Перебираем текстовые блоки внутри фигуры
        shape.text?.forEach { textBlock ->
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

}