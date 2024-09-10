package com.example.practiceandroid.views

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun DeleteDialog(
    showDeleteDialog: MutableState<Boolean>,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { showDeleteDialog.value = false },
        confirmButton = {
            TextButton(onClick = {
                onDelete()
                showDeleteDialog.value = false
            }) {
                Text("Да")
            }
        },
        dismissButton = {
            TextButton(onClick = { showDeleteDialog.value = false }) {
                Text("Нет")
            }
        },
        title = { Text("Подтверждение удаления") },
        text = { Text("Вы уверены, что хотите удалить эту фигуру?") }
    )
}
