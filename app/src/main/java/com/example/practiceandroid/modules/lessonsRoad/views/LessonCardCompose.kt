package com.example.practiceandroid.modules.lessonsRoad.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.practiceandroid.ConfigData
import com.example.practiceandroid.R
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadViewModel

/**
 * Функция, отвечающая за отображение кружка урока,
 * когда первый урок находится слева
 *
 * @param lessonsRoadViewModel
 * @param lesson: информация о уроке
 * @param position: порядковый номер урока в списке всех занятий
 * @param isFirstOrLastLessonInChapter: разделение случаев,
 * когда урок или первый в разделе, или последний
 */
@Composable
fun LessonCardLeftCompose(lessonsRoadViewModel: LessonsRoadViewModel, lesson: Map<String, String>, position: Int, isFirstOrLastLessonInChapter: Boolean) {

    val statusText = lesson["status"]?.let {
        lessonsRoadViewModel.setLessonStatusNameById(it)
    } ?: "Готово"

    val statusColor = lesson["status"]?.let {
        Color(lessonsRoadViewModel.getLessonsStatusColorById(it))
    } ?: Color(0xFF4CAF50)

    Box(
        contentAlignment = Alignment.TopStart,
        modifier = if (position % 2 == 0 || isFirstOrLastLessonInChapter)
            Modifier.fillMaxWidth().wrapContentHeight().padding(50.dp, 0.dp, 0.dp, 0.dp)
        else
            Modifier.fillMaxWidth().height(240.dp).padding(50.dp, 0.dp, 0.dp, 0.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .width(130.dp)
                .wrapContentHeight()
        ) {
            val lessonAddress = lesson["lesson_img_adr"]
            if (lessonAddress != null) {
                AsyncImage(
                    model = ConfigData.BASE_URL + lessonAddress,
                    contentDescription = "lesson_avatar",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .width(120.dp)
                        .height(120.dp)
                        .clip(RoundedCornerShape(60.dp))
                        .border(4.dp, Color.White, CircleShape)
                        .graphicsLayer {
                            this.scaleX = 1.21f
                            this.scaleY = 1.21f
                        }
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.no_lesson_image),
                    contentDescription = "lesson_avatar",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .width(120.dp)
                        .height(120.dp)
                        .clip(RoundedCornerShape(60.dp))
                        .border(4.dp, Color.White, CircleShape)
                        .graphicsLayer {
                            this.scaleX = 1.21f
                            this.scaleY = 1.21f
                        }
                )
            }

            Box(
                modifier = Modifier
                    .offset(0.dp, (-10).dp)
            ) {
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(22.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(statusColor)
                ) {
                    Text(
                        text = statusText,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontFamily = FontFamily(
                            Font(R.font.montserrat_semibold, FontWeight.SemiBold)
                        ),
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(10.dp, 4.dp, 10.dp, 4.dp)
                    )
                }
            }
            Text(
                text = "${lesson["lesson_number"]}." + "\u00A0" + "${lesson["lesson_short_name"]}",
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.lesson_text_color),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                fontSize = 13.sp,
                fontFamily = FontFamily(
                    Font(R.font.montserrat_medium_new, FontWeight.Medium)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .offset(0.dp, (-6).dp)
            )
        }
    }
}

/**
 * Функция, отвечающая за отображение кружка урока,
 * когда первый урок находится справа
 *
 * @param lessonsRoadViewModel
 * @param lesson: информация о уроке
 * @param position: порядковый номер урока в списке всех занятий
 * @param isFirstOrLastLessonInChapter: разделение случаев,
 * когда урок или первый в разделе, или последний
 */
@Composable
fun LessonCardRightCompose(lessonsRoadViewModel: LessonsRoadViewModel, lesson: Map<String, String>, position: Int, isFirstOrLastLessonInChapter: Boolean) {

    val statusText = lesson["status"]?.let {
        lessonsRoadViewModel.setLessonStatusNameById(it)
    } ?: "Готово"

    val statusColor = lesson["status"]?.let {
        Color(lessonsRoadViewModel.getLessonsStatusColorById(it))
    } ?: Color(0xFF4CAF50)

    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = if (position % 2 == 0 || isFirstOrLastLessonInChapter)
            Modifier.fillMaxWidth().wrapContentHeight().padding(0.dp, 0.dp, 50.dp, 0.dp)
        else
            Modifier.fillMaxWidth().height(240.dp).padding(0.dp, 0.dp, 50.dp, 0.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .width(130.dp)
                .wrapContentHeight()
        ) {
            val lessonAddress = lesson["lesson_img_adr"]
            if (lessonAddress != null) {
                AsyncImage(
                    model = ConfigData.BASE_URL + lessonAddress,
                    contentDescription = "lesson_avatar",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .width(120.dp)
                        .height(120.dp)
                        .clip(RoundedCornerShape(60.dp))
                        .border(4.dp, Color.White, CircleShape)
                        .graphicsLayer {
                            this.scaleX = 1.21f
                            this.scaleY = 1.21f
                        }
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.no_lesson_image),
                    contentDescription = "lesson_avatar",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .width(120.dp)
                        .height(120.dp)
                        .clip(RoundedCornerShape(60.dp))
                        .border(4.dp, Color.White, CircleShape)
                        .graphicsLayer {
                            this.scaleX = 1.21f
                            this.scaleY = 1.21f
                        }
                )
            }

            Box(
                modifier = Modifier
                    .offset(0.dp, (-10).dp)
            ) {
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(22.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(statusColor)
                ) {
                    Text(
                        text = statusText,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontFamily = FontFamily(
                            Font(R.font.montserrat_semibold, FontWeight.SemiBold)
                        ),
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(10.dp, 4.dp, 10.dp, 4.dp)
                    )
                }
            }
            Text(
                text = "${lesson["lesson_number"]}." + "\u00A0" + "${lesson["lesson_short_name"]}",
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.lesson_text_color),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                fontSize = 13.sp,
                fontFamily = FontFamily(
                    Font(R.font.montserrat_medium_new, FontWeight.Medium)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .offset(0.dp, (-6).dp)
            )
        }
    }
}

@Preview
@Composable
fun LessonCardComposePreview() {
    val lesson = mapOf(
        Pair("lesson_number", "39"),
        Pair("lesson_name", "Политические партии"),
        Pair("lesson_short_name", "Политические партии"),
        Pair("lesson_chapter", "Политика"),
        Pair("lesson_img_adr", "/images/lessons/39.png"),
        Pair("status", "3")
    )

    LessonCardLeftCompose(viewModel(), lesson, 0, true)
}

