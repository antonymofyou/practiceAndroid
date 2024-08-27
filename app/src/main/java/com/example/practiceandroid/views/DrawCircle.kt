package com.example.practiceandroid.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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

// Компонент для отрисовки круга на основе данных, переданных в объекте shape
@Composable
fun DrawCircle(shape: ResponseShapes.Shape) {
    Row(
        modifier = Modifier
            .graphicsLayer(
                rotationZ = shape.rotation ?: 0f,
                translationX = shape.x,
                translationY = shape.y,
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
            .padding(16.dp)
            .width(shape.width.dp)
            .height(shape.height.dp)
            .zIndex(shape.zIndex),
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