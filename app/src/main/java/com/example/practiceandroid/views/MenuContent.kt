package com.example.practiceandroid.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Composable-функция для отображения содержимого меню.
 *
 * @param widthContentMenu ширина содержимого меню.
 * @param heightContentMenu высота содержимого меню.
 */
@Composable
fun MenuContent(widthContentMenu: Dp, heightContentMenu: Dp) {
    Column(
        modifier = Modifier
            .height(heightContentMenu)
            .width(widthContentMenu)
            .background(Color.LightGray)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {},
        ) {
            Text(text = "Button1")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {},
        ) {
            Text(text = "Button2")
        }
    }
}