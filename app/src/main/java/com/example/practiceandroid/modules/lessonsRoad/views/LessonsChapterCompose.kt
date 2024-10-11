package com.example.practiceandroid.modules.lessonsRoad.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadViewModel
import com.example.practiceandroid.R
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsViewModel

/**
 * Компонент для отображения раздела уроков.
 *
 * @param lessonsRoadViewModel ViewModel, управляющая состоянием экрана с разделами уроков.
 * @param lessonsViewModel ViewModel, управляющая состоянием и логикой уроков.
 * @param chapter Список уроков, относящихся к конкретному разделу.
 * @param viewType Тип представления, используемый для отображения уроков.
 * @param index Порядковый номер первого урока текущего раздела в общем списке уроков.
 * @param lastIndex Порядковый номер последнего урока в общем списке уроков.
 * @param indexGroupedLessons Индекс текущего раздела в сгруппированном списке разделов.
 */
@Composable
fun LessonsChapterCompose(
    lessonsRoadViewModel: LessonsRoadViewModel,
    lessonsViewModel: LessonsViewModel,
    chapter: ArrayList<Map<String, String>>,
    viewType: Int,
    index: Int,
    lastIndex: Int,
    indexGroupedLessons: Int,
) {

    // Задание цвета заднего фона раздела
    val chapterName = chapter[0]["lesson_chapter"] ?: "Неизвестный раздел"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

        // Блок с кружками уроков
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .onGloballyPositioned { coordinates ->
                    lessonsRoadViewModel.columnsHeightList[indexGroupedLessons] = lessonsViewModel.pxToDp(coordinates.size.height).dp
                }
        ) {
            val screenWidthDp = with(LocalConfiguration.current) { screenWidthDp }

            // Объект, отвечающий за представление кружков
            val lessonCardView = LessonCardView(lessonsRoadViewModel, lessonsViewModel, screenWidthDp)

            // порядковый номер первого урока раздела в списке всех уроков
            var position = index
            chapter.forEachIndexed { index, lesson ->
                if (viewType == VIEW_TYPE_LEFT) {
                    if (position % 2 == 0) {
                        lessonCardView.LessonCardLeftCompose(lesson, position, index == 0, index == chapter.lastIndex, lastIndex)
                    } else {
                        lessonCardView.LessonCardRightCompose(lesson, position, index == 0, index == chapter.lastIndex, lastIndex)
                    }
                } else {
                    if (position % 2 == 0) {
                        lessonCardView.LessonCardRightCompose(lesson, position, index == 0, index == chapter.lastIndex, lastIndex)
                    } else {
                        lessonCardView.LessonCardLeftCompose(lesson, position, index == 0, index == chapter.lastIndex, lastIndex)
                    }
                }
                ++position
            }
        }

        // Отображение плашек с названием раздела
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .height(lessonsRoadViewModel.columnsHeightList[indexGroupedLessons] ?: 0.dp)
        ) {
            // Верхняя
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 24.dp, 16.dp, 24.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.White)
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = chapterName,
                    fontFamily = FontFamily(
                        Font(R.font.montserrat_bold, FontWeight.Bold)
                    ),
                    color = colorResource(id = R.color.lesson_text_color),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }

            // Нижняя
            Box (
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier.fillMaxSize()
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 24.dp, 16.dp, 24.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.White)
                        .height(50.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = chapterName,
                        fontFamily = FontFamily(
                            Font(R.font.montserrat_bold, FontWeight.Bold)
                        ),
                        color = colorResource(id = R.color.lesson_text_color),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun LessonsChapterComposePreview() {
//    val lessonChapter = arrayListOf( mapOf(
//        Pair("lesson_number", "39"),
//        Pair("lesson_name", "Политические партии"),
//        Pair("lesson_short_name", "Политические партии"),
//        Pair("lesson_chapter", "Политика"),
//        Pair("lesson_img_adr", "/images/lessons/39.png"),
//        Pair("status", "3")
//    ) )
//
//    LessonsChapterCompose(viewModel(), lessonChapter, VIEW_TYPE_LEFT, 0, 0, imageBrush)
//}