package com.example.practiceandroid.views

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.practiceandroid.viewModels.MenuViewModel

const val DURATION_MILLIS = 430 // Длительность анимации в миллисекундах
const val START_HEIGHT_OPEN_BUTTON_DP = 100 // Начальная высота кнопки открытия меню в dp
const val OFFSET_IF_OPEN_MENU = 0f // Смещение при открытом меню

/**
 * Composable-функция для отображения навигации меню с анимацией.
 *
 * @param viewModel ViewModel для управления состоянием меню.
 * @param widthContentMenu ширина содержимого меню.
 * @param heightContentMenu высота содержимого меню.
 */
@Composable
fun MenuNavigation(
    viewModel: MenuViewModel,
    widthContentMenu: Dp,
    heightContentMenu: Dp
) {

    // Анимация смещения по оси X для меню
    val animatedOffsetX by animateFloatAsState(
        targetValue = if (viewModel.isOpen.value) {
            OFFSET_IF_OPEN_MENU
        } else {
            -with(LocalDensity.current) { widthContentMenu.toPx() }
        },
        animationSpec = tween(durationMillis = DURATION_MILLIS),
        label = "",
    )

    // Анимация изменения высоты кнопки открытия меню
    val animatedHeight by animateDpAsState(
        targetValue =
        if (viewModel.isOpen.value) {
            heightContentMenu
        } else START_HEIGHT_OPEN_BUTTON_DP.dp,
        animationSpec = tween(durationMillis = DURATION_MILLIS), label = "",
    )

    Row(
        Modifier.graphicsLayer {
            translationX = animatedOffsetX
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        MenuContent(widthContentMenu, heightContentMenu)

        MenuToggleButton(
            viewModel = viewModel,
            animatedHeight = animatedHeight
        )
    }
}

