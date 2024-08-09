package com.example.practiceandroid.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MenuViewModel : ViewModel() {

    /**
     * Хранит состояние меню: открыто или закрыто.
     */
    var isOpen = mutableStateOf(false)
        private set

    /**
     * Открывает меню.
     */
    fun openMenu() {
        isOpen.value = true
    }

    /**
     * Закрывает меню.
     */
    fun closeMenu() {
        isOpen.value = false
    }

}