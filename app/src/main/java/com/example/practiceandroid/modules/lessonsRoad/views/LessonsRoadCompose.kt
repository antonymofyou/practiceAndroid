package com.example.practiceandroid.modules.lessonsRoad.views

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practiceandroid.R
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadViewModel
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

// Константы для определения типа view
val VIEW_TYPE_RIGHT = 0
val VIEW_TYPE_LEFT = 1

/**
 * Функция, отвечающая за дорожку уроков
 */
@Composable
fun LessonsRoadCompose(
    viewType: Int,
    lessonsRoadViewModel: LessonsRoadViewModel = viewModel<LessonsRoadViewModel>(),
    lessonsViewModel: LessonsViewModel = viewModel<LessonsViewModel>()
) {
    // Параметры для скролла
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Условие для отображения кнопки поднятия
    val showButton = remember {
        derivedStateOf {
            val firstHeight = lessonsRoadViewModel.columnsHeightList.getOrNull(0)?.value
            if (firstHeight != null) {
                scrollState.value > lessonsViewModel.dpToPx(firstHeight)
            } else {
                false
            }
        }
    }

    val density = LocalDensity.current.density
    val screenWidthPx = with(LocalConfiguration.current) { screenWidthDp * density }
    lessonsRoadViewModel.setImageBrush(screenWidthPx)

    // Отслеживание изменения высот с задержкой в 0.5 сек
    LaunchedEffect(lessonsRoadViewModel.columnHeight.value) {
        snapshotFlow { lessonsRoadViewModel.columnHeight.value }
            .debounce(4)
            .collect { updatedHeights ->
                if (lessonsRoadViewModel.columnHeight.value != 0.dp) {
                    lessonsRoadViewModel.allHeightsMeasured.value = true
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (lessonsRoadViewModel.allHeightsMeasured.value) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .graphicsLayer {
                        translationY = .2f * scrollState.value
                    }
            ) {
                var index = 0
                for (chapter in lessonsRoadViewModel.groupedLessons) {
                    val chapterName = chapter[0]["lesson_chapter"] ?: "Неизвестный раздел"

                    // Цвет фона
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(
                                    lessonsRoadViewModel.getBackgroundColorForChapter(
                                        chapterName
                                    )
                                )
                            )
                            .height(lessonsRoadViewModel.columnsHeightList[index]!!) // Используем высоту только если она не null
                    )
                    index += 1
                }

                // Добавочная высота для прогрузки всей дорожки.
                val chapterName = lessonsRoadViewModel.groupedLessons.last()[0]["lesson_chapter"] ?: "Неизвестный раздел"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(lessonsRoadViewModel.columnHeight.value * .175f)
                        .background(
                            Color(lessonsRoadViewModel.getBackgroundColorForChapter(chapterName)).copy(
                                alpha = 0.5f
                            )
                        )
                )
            }

            // Фоновый параллакс
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        translationY = -.2f * scrollState.value
                    }
                    .background(lessonsRoadViewModel.imageBrush.value!!)
                    .requiredHeight(lessonsRoadViewModel.columnHeight.value)
            )
        }

        // Столбец для отображения всех разделов
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .graphicsLayer {
                    translationY = .2f * scrollState.value
                }
                .onGloballyPositioned { coordinates ->
                    lessonsRoadViewModel.columnHeight.value = lessonsViewModel.pxToDp(coordinates.size.height).dp
                }
                .alpha(if (lessonsRoadViewModel.allHeightsMeasured.value) 1f else 0f)
        ) {
            var index = 0   // общий порядковый номер уроков
            var indexGroupedLessons = 0 // индекс как полная группа

            val lastIndex = lessonsRoadViewModel.groupedLessons.map { it.size }
                .sum() - 1  // индекс последнего урока
            for (chapter in lessonsRoadViewModel.groupedLessons) {
                LessonsChapterCompose(
                    lessonsRoadViewModel,
                    lessonsViewModel,
                    chapter,
                    viewType,
                    index,
                    lastIndex,
                    indexGroupedLessons,
                )
                index += chapter.size
                indexGroupedLessons += 1
            }

            // Добавочная высота для прогрузки всей дорожки.
            // Без неё часть дорожки обрезается.
            val chapterName = lessonsRoadViewModel.groupedLessons.last()[0]["lesson_chapter"]
                ?: "Неизвестный раздел"

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(lessonsRoadViewModel.columnHeight.value * .175f)
                    .background(
                        Color(lessonsRoadViewModel.getBackgroundColorForChapter(chapterName)).copy(
                            alpha = 0.5f
                        )
                    )
            )
        }

        if (lessonsRoadViewModel.allHeightsMeasured.value) {
            GoToTop(coroutineScope, showButton, scrollState)
        }
    }

    LaunchedEffect(lessonsRoadViewModel.allHeightsMeasured.value) {
        if (lessonsRoadViewModel.isFirstOpen && lessonsRoadViewModel.allHeightsMeasured.value) {
            coroutineScope.launch {
                scrollState.animateScrollTo(scrollState.maxValue + lessonsRoadViewModel.columnHeight.value.value.toInt())
            }
            lessonsRoadViewModel.isFirstOpen = false
        }
    }
}

@Composable
fun GoToTop(coroutineScope: CoroutineScope, showButton: State<Boolean>, scrollState: ScrollState) {
    // Создаем анимируемое значение для alpha
    val alphaValue = remember { Animatable(0f) }

    // LaunchedEffect для анимации альфа-значения
    LaunchedEffect(showButton.value) {
        if (showButton.value) {
            alphaValue.animateTo(1f, animationSpec = tween(durationMillis = 300)) // Появление
        } else {
            alphaValue.animateTo(0f, animationSpec = tween(durationMillis = 300)) // Исчезновение
        }
    }

    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier
            .fillMaxSize()
            .alpha(alphaValue.value)
    ) {
        FloatingActionButton(
            modifier = Modifier
                .padding(30.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.White),
            onClick = {
                if (showButton.value) {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0)
                    }
                }
            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.upbtn),
                contentDescription = "up button"
            )
        }
    }
}

@Preview
@Composable
fun LessonsRoadComposePreview(){
    LessonsRoadCompose(VIEW_TYPE_LEFT)
}
