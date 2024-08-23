package com.example.practiceandroid.modules.lessonsRoad.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadViewModel
import com.example.practiceandroid.R

/**
 * Функция, за отображение раздела
 *
 * @param lessonsRoadViewModel: view model
 * @param chapter: список уроков по конкретному разделу
 * @param viewType: тип view
 * @param index: порядковый номер первого урока из раздела в общем списке занятий
 */
@Composable
fun LessonsChapterCompose(
    lessonsRoadViewModel: LessonsRoadViewModel,
    chapter: ArrayList<Map<String, String>>,
    viewType: Int,
    index: Int
) {

    val chapterName = chapter[0]["lesson_chapter"] ?: "Неизвестный раздел"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            // TODO Заливка цветом перекрывает фон
            .background(
                Color(lessonsRoadViewModel.getBackgroundColorForChapter(chapterName)).copy(
                    alpha = 0.5f
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
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
                    textAlign = TextAlign.Center,
                )
            }

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                var position = index
                chapter.forEachIndexed { index, lesson ->
                    if (viewType == VIEW_TYPE_LEFT) {
                        if (position % 2 == 0) {
                            LessonCardLeftCompose(lessonsRoadViewModel, lesson, position, (index == 0 || index == chapter.lastIndex))
                        } else {
                            LessonCardRightCompose(lessonsRoadViewModel, lesson, position, (index == 0 || index == chapter.lastIndex))
                        }
                    } else {
                        if (position % 2 == 0) {
                            LessonCardRightCompose(lessonsRoadViewModel, lesson, position, (index == 0 || index == chapter.lastIndex))
                        } else {
                            LessonCardLeftCompose(lessonsRoadViewModel, lesson, position, (index == 0 || index == chapter.lastIndex))
                        }
                    }
                    ++position
                }
            }

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

@Preview
@Composable
fun LessonsChapterComposePreview() {
    val lessonChapter = arrayListOf( mapOf(
        Pair("lesson_number", "39"),
        Pair("lesson_name", "Политические партии"),
        Pair("lesson_short_name", "Политические партии"),
        Pair("lesson_chapter", "Политика"),
        Pair("lesson_img_adr", "/images/lessons/39.png"),
        Pair("status", "3")
    ) )

    LessonsChapterCompose(viewModel(), lessonChapter, VIEW_TYPE_LEFT, 0)
}