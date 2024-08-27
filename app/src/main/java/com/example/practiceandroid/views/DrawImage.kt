package com.example.practiceandroid.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.practiceandroid.models.ResponseShapes

/**
    Компонент для отрисовки изображения, переданного через image и отрисовка с учетом
    параметров из shape
 */
@Composable
fun DrawImage(shape: ResponseShapes.Shape, image: ImageBitmap) {
    Image(
        bitmap = image,
        contentDescription = null,
        modifier = Modifier
            .graphicsLayer(
                rotationZ = shape.rotation ?: 0f,
                translationX = shape.x,
                translationY = shape.y,
            )
            .width(shape.width.dp)
            .height(shape.height.dp)
            .zIndex(shape.zIndex)
            .clip(shape = RoundedCornerShape(shape.cornerRadius?.dp ?: 0.dp))
    )
}