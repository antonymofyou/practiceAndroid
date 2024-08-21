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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.transform
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

    // Скейлим изображение по ширине
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.lessons_road_bg)

    val density = LocalDensity.current.density
    val screenWidthPx = with(LocalConfiguration.current) { screenWidthDp * density }

    val imageWidth = imageBitmap.width.toFloat()

    val scaleX = screenWidthPx / imageWidth
    val scaleY = scaleX

    // Устанавливаем фон
    val scaledShader = ImageShader(
        imageBitmap,
        tileModeY = TileMode.Repeated
    ).apply {
        transform {
            setScale(scaleX, scaleY)
        }
    }

    val imageBrush = ShaderBrush(scaledShader)

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(imageBrush)
                .graphicsLayer {
                    translationY = .2f * scrollState.value
                }
        )
        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            for (chapter in lessonsRoadViewModel.groupedLessons) {
                LessonsChapterCompose(lessonsRoadViewModel, chapter)
            }
        }
    }
}

@Preview
@Composable
fun LessonsRoadComposePreview(){
    LessonsRoadCompose()
}
