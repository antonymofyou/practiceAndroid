package com.example.practiceandroid.views.contextmenu.border

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Функции для проверки ввода
fun validateBorderWidth(value: String): Boolean {
    val floatValue = value.toFloatOrNull() ?: return false
    return floatValue in 0f..10f
}

fun validateCornerRadius(value: String): Boolean {
    val floatValue = value.toFloatOrNull() ?: return false
    return floatValue in 0f..70f
}

@Composable
fun ChangeSettingBorderDialog(
    showChangeBorderSettingDialog: MutableState<Boolean>,
    showChangeBorderColorDialog: MutableState<Boolean>,
    borderColor: MutableState<String>,
    borderWidth: MutableState<Dp>,
    cornerRadius: MutableState<Dp>,
) {
    var selectedBorderWidth = rememberSaveable { mutableStateOf(borderWidth.value.value.toString())}
    var selectedCornerRadius = rememberSaveable { mutableStateOf(cornerRadius.value.value.toString())}

    val isConfirmButtonEnabled = validateBorderWidth(selectedBorderWidth.value) && validateCornerRadius(selectedCornerRadius.value)

    AlertDialog(
        onDismissRequest = { showChangeBorderSettingDialog.value = false },
        confirmButton = {
            TextButton(onClick = {
                borderWidth.value = selectedBorderWidth.value.toFloat().dp
                cornerRadius.value = selectedCornerRadius.value.toFloat().dp
                showChangeBorderSettingDialog.value = false
            },
                enabled = isConfirmButtonEnabled) {
                Text("ОК")
            }
        },
        dismissButton = {
            TextButton(onClick = { showChangeBorderSettingDialog.value = false }) {
                Text("Отмена")
            }
        },
        title = { Text("Изменить параметры границы") },
        text = {

            val scrollState = rememberScrollState()

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth().verticalScroll(scrollState),

            ){
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Цвет",
                        style = MaterialTheme.typography.bodyMedium, // Установка стиля текста
                        modifier = Modifier.padding(bottom = 4.dp)// Отступ между меткой и кнопкой
                    )
                    Button(
                        onClick = {
                            showChangeBorderColorDialog.value = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(android.graphics.Color.parseColor(borderColor.value)),
                            contentColor = androidx.compose.ui.graphics.Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {}
                }

                BorderAndCornerRadiusInputFields(
                    selectedBorderWidth,
                    selectedCornerRadius
                )
            }
        }
    )
}

@Composable
fun ChangeSettingBorderDialog(
    showChangeBorderSettingDialog: MutableState<Boolean>,
    showChangeBorderColorDialog: MutableState<Boolean>,
    borderColor: MutableState<String>,
    borderWidth: MutableState<Dp>
) {
    var selectedBorderWidth = rememberSaveable { mutableStateOf(borderWidth.value.value.toString())}

    val isConfirmButtonEnabled = validateBorderWidth(selectedBorderWidth.value)

    AlertDialog(
        onDismissRequest = { showChangeBorderSettingDialog.value = false },
        confirmButton = {
            TextButton(onClick = {
                borderWidth.value = selectedBorderWidth.value.toFloat().dp
                showChangeBorderSettingDialog.value = false
            },
                enabled = isConfirmButtonEnabled) {
                Text("ОК")
            }
        },
        dismissButton = {
            TextButton(onClick = { showChangeBorderSettingDialog.value = false }) {
                Text("Отмена")
            }
        },
        title = { Text("Изменить параметры границы") },
        text = {

            val scrollState = rememberScrollState()

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth().verticalScroll(scrollState),

                ){
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Цвет",
                        style = MaterialTheme.typography.bodyMedium, // Установка стиля текста
                        modifier = Modifier.padding(bottom = 4.dp)// Отступ между меткой и кнопкой
                    )
                    Button(
                        onClick = {
                            showChangeBorderColorDialog.value = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(android.graphics.Color.parseColor(borderColor.value)),
                            contentColor = androidx.compose.ui.graphics.Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {}
                }

                BorderAndCornerRadiusInputFields(
                    selectedBorderWidth
                )
            }
        }
    )
}