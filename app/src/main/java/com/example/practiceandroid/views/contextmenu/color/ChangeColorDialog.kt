package com.example.practiceandroid.views.contextmenu.color

import android.graphics.Color
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun ChangeColorDialog(
    showChangeColorDialog: MutableState<Boolean>,
    currentColor: MutableState<String>,
    label: String
) {
    val initialColor = android.graphics.Color.parseColor(currentColor.value)
    var selectedColor by rememberSaveable { mutableStateOf(initialColor) } // Хранение выбранного цвета

    var isConfirmButtonEnabled = rememberSaveable {mutableStateOf(false)}

    AlertDialog(
        onDismissRequest = { showChangeColorDialog.value = false },
        confirmButton = {
            TextButton(onClick = {
                // Обновляем цвет только при нажатии на ОК
                currentColor.value = "#${Integer.toHexString(selectedColor)}"
                showChangeColorDialog.value = false
            },
                enabled = isConfirmButtonEnabled.value
            ) {
                Text("ОК")
            }
        },
        dismissButton = {
            TextButton(onClick = { showChangeColorDialog.value = false }) {
                Text("Отмена")
            }
        },
        title = { Text(label) },
        text = {
            // Передаем в HSLColorPicker функцию для обновления выбранного цвета
            HSLColorPicker(selectedColor, isConfirmButtonEnabled) { color ->
                selectedColor = Color.parseColor(color)
            }
        }
    )
}