package com.example.practiceandroid.modules.lessonsRoad.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practiceandroid.R
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadViewModel

/**
 * Функция, отвечающая за дорожку уроков
 */
@Preview
@Composable
fun LessonsRoadCompose() {
    val scrollState = rememberScrollState()

    // Получение сгруппированных по разделам уроков
    val lessonsRoadViewModel = viewModel<LessonsRoadViewModel>()
    val roadList = arrayListOf<Map<String, String>>()
    if (lessonsRoadViewModel.lessonsRoadList != null) {
        // Установка списка дорожки уроков
        for (road in lessonsRoadViewModel.lessonsRoadList!!) {
            roadList.add(road)
        }
        lessonsRoadViewModel.groupedLessons =
            lessonsRoadViewModel.getLessonsByChapter(roadList)
    }

    // Фоновое изображение, которое может повторяться
    val imageBrush =
        ShaderBrush(
            ImageShader(
                ImageBitmap.imageResource(
                    id = R.drawable.lessons_road_bg
                ),
                tileModeX = TileMode.Repeated,
                tileModeY = TileMode.Repeated
            )
        )

    Box(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .background(
                imageBrush
            )
            .graphicsLayer {
                translationY = .2f * scrollState.value  // Замедление смещения слоя при скролле
            }
    ) {
        Column {
            for (chapter in lessonsRoadViewModel.groupedLessons) {
                LessonsChapterCompose(lessonsRoadViewModel, chapter)
            }
        }
    }
}
