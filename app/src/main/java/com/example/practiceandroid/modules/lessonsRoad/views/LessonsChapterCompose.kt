package com.example.practiceandroid.modules.lessonsRoad.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadViewModel

/**
 * Функция, за отображение раздела
 *
 * @param lessonsRoadViewModel: view model
 * @param chapter: список уроков по конкретному разделу
 */
@Composable
fun LessonsChapterCompose(lessonsRoadViewModel: LessonsRoadViewModel, chapter: ArrayList<Map<String, String>>) {

    val chapterName = chapter[0]["lesson_chapter"] ?: "Неизвестный раздел"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp) // временно для лучшего отображения
            // TODO Заливка цветом перекрывает фон
            .background(Color(lessonsRoadViewModel.getBackgroundColorForChapter(chapterName)).copy(alpha = 0.5f))
    ) {
        Text(
            text = chapterName
        )
        // TODO
    }
}