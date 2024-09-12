package com.example.practiceandroid.modules.lessonsRoad.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.transform
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practiceandroid.R
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadViewModel
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsViewModel

// Константы для определения типа view
val VIEW_TYPE_RIGHT = 0
val VIEW_TYPE_LEFT = 1

/**
 * Функция, отвечающая за дорожку уроков
 */
@Composable
fun LessonsRoadCompose(viewType: Int) {
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

    // Высота столбца всех разделов
    var columnHeight by remember {
        mutableStateOf(0.dp)
    }

    val lessonsViewModel = viewModel<LessonsViewModel>()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Столбец для отображения всех разделов
        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(imageBrush)
                .graphicsLayer {
                    translationY = .2f * scrollState.value
                }
                .onGloballyPositioned { coordinates ->
                    columnHeight = lessonsViewModel.pxToDp(coordinates.size.height).dp
                }
        ) {
            var index = 0   // общий порядковый номер уроков
            val lastIndex = lessonsRoadViewModel.groupedLessons.map { it.size }.sum() - 1  // индекс последнего урока
            for (chapter in lessonsRoadViewModel.groupedLessons) {
                LessonsChapterCompose(lessonsRoadViewModel, chapter, viewType, index, lastIndex)
                index += chapter.size
            }

            // Добавочная высота для прогрузки всей дорожки.
            // Без неё часть дорожки обрезается.
            val chapterName = lessonsRoadViewModel.groupedLessons.last()[0]["lesson_chapter"] ?: "Неизвестный раздел"
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(columnHeight * .175f)
                    .background(
                        Color(lessonsRoadViewModel.getBackgroundColorForChapter(chapterName)).copy(
                            alpha = 0.5f
                        )
                    )
            )
        }
    }
}

@Preview
@Composable
fun LessonsRoadComposePreview(){
    LessonsRoadCompose(VIEW_TYPE_LEFT)
}
