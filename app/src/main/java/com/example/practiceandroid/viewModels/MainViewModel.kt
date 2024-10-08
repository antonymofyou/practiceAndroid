package com.example.practiceandroid.viewModels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class MainViewModel(app: Application): AndroidViewModel(app) {

    val standard = mapOf(
        "id" to "38",
        "name" to "1245",
        "link" to "",
        "process" to "1111",
        "updatedAt" to "7 октября 2024 г. в 18:14",
        "changerUserId" to "5498698",
        "changerUserName" to "Антон Шмонин",
        "canEdit" to "0",
        "learnedAt" to "",
        "isLearned" to "0"
    )
    val isShowPopUp = mutableStateOf(false)

    val popUpState = mutableStateOf(StandardsPopupStatus.LOADING)
}

// для отслеживания состояния загрузки экрана
enum class StandardsPopupStatus {
    LOADING, LOADED
}