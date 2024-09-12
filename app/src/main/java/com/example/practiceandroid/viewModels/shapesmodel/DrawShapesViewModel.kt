package com.example.practiceandroid.viewModels.shapesmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class DrawShapesViewModel: ViewModel() {

    // Масштабирование для отрисовки фигур (начальное значение — 1f, т.е. без масштабирования)
    var scale: MutableState<Float> = mutableStateOf(1f)
}