package com.example.practiceandroid.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.practiceandroid.ext.valueOf
import com.example.practiceandroid.models.ResponseShapes

// Компонент для отрисовки прямоугольника на основе данных, переданных в объекте shape
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawRectangle(shape: ResponseShapes.Shape, focusManager: FocusManager) {

    // Контейнер Row для размещения текста внутри прямоугольника
    Row(
        modifier = Modifier
            .graphicsLayer(
                rotationZ = shape.rotation ?: 0f,
                translationX = shape.x,
                translationY = shape.y,
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
            .padding(16.dp)
            .width(shape.width.dp)
            .height(shape.height.dp)
            .zIndex(shape.zIndex)
            .clickable { focusManager.clearFocus() },
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

//"text": [
//{
//    "alignment": "left",
//    "text": [
//    {
//        "text": "Hello",
//        "type": "bold",
//        "fontSize": 24,
//        "fontColor": "#333333",
//        "textDecoration": "underline"
//    },
//    {
//        "text": " This is a test text.",
//        "fontSize": 18,
//        "fontColor": "#333333"
//    }
//    ]
//},