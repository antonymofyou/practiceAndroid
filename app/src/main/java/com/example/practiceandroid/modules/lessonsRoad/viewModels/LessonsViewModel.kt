package com.example.practiceandroid.modules.lessonsRoad.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class LessonsViewModel(private val app: Application) : AndroidViewModel(app) {
    var lessonStatus = MutableLiveData<LessonStatus>(LessonStatus.LESSONS_ROAD)
    var currentLesson = MutableLiveData(mapOf<String, String>())
    var currentLessonNum = MutableLiveData("")
    var isScreenLarge = false
    var isDopPart = ""
    var videoId: String = ""
    var htP1Num: String = ""
    var htP2Num: String = ""
    var autoZachetNum: String = ""
    var verbalZachetNum: String = ""
    var isBtnBackVisible = MutableLiveData(true)

    // Конвертация пикселей в дп
    fun pxToDp(px: Int): Int {
        return (px / app.resources.displayMetrics.density).toInt()
    }

    // Конвертация дп в пиксели
    fun dpToPx(dp: Int): Int {
        return (dp * app.resources.displayMetrics.density).toInt()
    }

    // Конвертация дп в пиксели
    fun dpToPx(dp: Float): Int {
        return (dp * app.resources.displayMetrics.density).toInt()
    }
}

enum class LessonStatus {
    LESSONS_ROAD, LESSON, VIDEO_LESSON, HOMETASK_P1, HOMETASK_P2, ZACHET_AUTO, ZACHET_VERBAL
}