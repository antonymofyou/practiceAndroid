package com.example.practiceandroid.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TopMenuViewModel: ViewModel() {
    private val _selectedItem = MutableStateFlow(0)
    val selectedItem: StateFlow<Int> get() = _selectedItem

    fun selectItem(index: Int) {
        _selectedItem.value = index
    }
}