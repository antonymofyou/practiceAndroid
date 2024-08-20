package com.example.practiceandroid.ext

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

//Расширение для класса [String], которое декодирует строку в формате Base64 в объект [ImageBitmap].
@OptIn(ExperimentalEncodingApi::class)
fun String.decodeBase64ToBitmap(): ImageBitmap {
    // Сохраняем текущее значение строки
    var image = this

    // Удаляем префикс, если он есть (например, "data:image/png;base64,")
    if (image.startsWith("data:image/png;base64,")) {
        image = image.replace("data:image/png;base64,", "")
    }

    // Декодирование строки Base64 в массив байтов
    val decodedString: ByteArray = Base64.decode(image)

    // Создание Bitmap из массива байтов
    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

    // Преобразуем Bitmap в ImageBitmap
    return decodedByte.asImageBitmap()
}