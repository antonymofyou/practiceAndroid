package com.example.practiceandroid.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.practiceandroid.models.ResponseShapes

class DrawShapesViewModel: ViewModel() {

    // Масштабирование для отрисовки фигур (начальное значение — 1f, т.е. без масштабирования)
    var scale: MutableState<Float> = mutableStateOf(1f)
}