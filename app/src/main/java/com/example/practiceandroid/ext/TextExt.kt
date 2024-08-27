package com.example.practiceandroid.ext

import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

// Преобразует строковое значение выравнивания по вертикали в соответствующее значение Alignment.Vertical
fun Alignment.Companion.valueOf(textVerticalAlignment: String?): Alignment.Vertical {
    return when (textVerticalAlignment) {
        "top" -> Alignment.Top
        "centerVertically" -> Alignment.CenterVertically
        "bottom" -> Alignment.Bottom
        else -> Alignment.Top
    }
}

// Преобразует строковое значение выравнивания текста в соответствующее значение TextAlign
fun TextAlign.Companion.valueOf(alignment: String): TextAlign {
    return when (alignment) {
        "left" -> TextAlign.Left
        "right" -> TextAlign.Right
        "center" -> TextAlign.Center
        "justify" -> TextAlign.Justify
        "start" -> TextAlign.Start
        "end" -> TextAlign.End
        "unspecified" -> TextAlign.Unspecified
        else -> TextAlign.Left
    }
}