package com.example.practiceandroid.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practiceandroid.viewModels.MenuViewModel

/**
 * Composable-функция для отображения меню.
 *
 * @param widthContentMenu ширина содержимого меню.
 * @param heightContentMenu высота содержимого меню.
 */
@Composable
fun Menu(widthContentMenu: Dp, heightContentMenu: Dp) {

    val viewModel: MenuViewModel = viewModel()
    MenuNavigation(
        viewModel = viewModel,
        widthContentMenu = widthContentMenu,
        heightContentMenu = heightContentMenu
    )
}