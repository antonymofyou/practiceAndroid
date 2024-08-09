package com.example.practiceandroid.views

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.practiceandroid.viewModels.MenuViewModel

/**
 * Composable-функция для отображения кнопки открытия/закрытия меню с анимацией.
 *
 * @param viewModel ViewModel для управления состоянием меню.
 * @param animatedHeight Анимированная высота кнопки.
 */
@Composable
fun MenuToggleButton(
    viewModel: MenuViewModel,
    animatedHeight: Dp
) {
    Box(
        modifier = Modifier
            .height(animatedHeight)
            .width(16.dp)
            .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
            .background(Color.LightGray)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > 0) {
                        viewModel.openMenu()
                    } else if (dragAmount < 0) {
                        viewModel.closeMenu()
                    }
                }
            }
    ) {
        // Иконка кнопки, меняется в зависимости от состояния меню
        val icon = if (viewModel.isOpen.value) {
            Icons.AutoMirrored.Filled.KeyboardArrowLeft
        } else {
            Icons.AutoMirrored.Filled.KeyboardArrowRight
        }

        Icon(
            modifier = Modifier.align(Alignment.Center),
            imageVector = icon,
            contentDescription = "Icon button"
        )
    }
}