package com.example.practiceandroid.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.foundation.text.BasicText
import java.util.regex.Pattern

/**
 * Компонент для отображения стилизованного текста
 *
 * @param rawText Входной текст с HTML тегами
 */
@Composable
fun StyledText(rawText: String) {
    val styledText = parseHTMLString(rawText)
    BasicText(text = styledText)
}

/**
 * Функция для парсинга HTML строки и создания AnnotatedString
 *
 * @param htmlString Входной текст с HTML тегами
 * @return AnnotatedString со стилями
 */
fun parseHTMLString(htmlString: String): AnnotatedString {
    val pattern = Pattern.compile("(<b>|<i>|<u>|<s>|</b>|</i>|</u>|</s>)")
    val matcher = pattern.matcher(htmlString)
    var lastEnd = 0

    // Используем билдер для создания AnnotatedString
    val result = buildAnnotatedString {
        var isBold = false
        var isItalic = false
        var isUnderline = false
        var isStrikethrough = false

        // Проходим по всем найденным тегам
        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()

            // Добавляем текст до текущего тега
            if (lastEnd < start) {
                appendStyledText(
                    htmlString.substring(lastEnd, start),
                    isBold,
                    isItalic,
                    isUnderline,
                    isStrikethrough
                )
            }

            // Обновляем стили на основе текущего тега
            when (matcher.group()) {
                "<b>" -> isBold = true
                "</b>" -> isBold = false
                "<i>" -> isItalic = true
                "</i>" -> isItalic = false
                "<u>" -> isUnderline = true
                "</u>" -> isUnderline = false
                "<s>" -> isStrikethrough = true
                "</s>" -> isStrikethrough = false
            }

            lastEnd = end
        }

        // Добавляем оставшийся текст после последнего тега
        if (lastEnd < htmlString.length) {
            appendStyledText(
                htmlString.substring(lastEnd),
                isBold,
                isItalic,
                isUnderline,
                isStrikethrough
            )
        }
    }

    return result
}

/**
 * Функция для добавления стилизованного текста в AnnotatedString.Builder
 *
 * @param text Текст для добавления
 * @param isBold Стиль - жирный текст
 * @param isItalic Стиль - курсивный текст
 * @param isUnderline Стиль - подчеркнутый текст
 * @param isStrikethrough Стиль - зачеркнутый текст
 */
fun AnnotatedString.Builder.appendStyledText(
    text: String,
    isBold: Boolean,
    isItalic: Boolean,
    isUnderline: Boolean,
    isStrikethrough: Boolean
) {
    withStyle(
        style = SpanStyle(
            fontWeight = if (isBold) androidx.compose.ui.text.font.FontWeight.Bold else null,
            fontStyle = if (isItalic) androidx.compose.ui.text.font.FontStyle.Italic else null,
            textDecoration = when {
                isUnderline && isStrikethrough -> TextDecoration.combine(
                    listOf(
                        TextDecoration.Underline,
                        TextDecoration.LineThrough
                    )
                )
                isUnderline -> TextDecoration.Underline
                isStrikethrough -> TextDecoration.LineThrough
                else -> null
            }
        )
    ) {
        append(text)
    }
}