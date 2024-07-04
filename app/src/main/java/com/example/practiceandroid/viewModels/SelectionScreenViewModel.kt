package com.example.practiceandroid.viewModels

import Cell
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SelectionScreenViewModel: ViewModel() {
    var showPopup by mutableStateOf(false)
        private set

    var selectedCell by mutableStateOf<Cell?>(null)
        private set

    fun onCellClick(cell: Cell) {
        selectedCell = cell
        showPopup = true
    }

    fun dismissPopup() {
        showPopup = false
    }
}