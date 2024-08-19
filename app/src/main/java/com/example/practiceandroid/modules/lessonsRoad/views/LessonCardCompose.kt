package com.example.practiceandroid.modules.lessonsRoad.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.practiceandroid.R
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadViewModel

/**
 * Функция, отвечающая за отображение кружка урока
 *
 * @param lessonsRoadViewModel
 * @param lesson: информация о уроке
 */
@Composable
fun LessonCardCompose(lessonsRoadViewModel: LessonsRoadViewModel, lesson: Map<String, String>) {

    val statusText = lesson["status"]?.let {
        lessonsRoadViewModel.setLessonStatusNameById(it)
    } ?: "Готово"

    val statusColor = lesson["status"]?.let {
        Color(lessonsRoadViewModel.getLessonsStatusColorById(it))
    } ?: Color(0xFF4CAF50)

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .width(130.dp)
            .height(240.dp)
    ) {
        // TODO Временно отображается изображение по умолчанию для всех уроков
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

        Box (
            modifier = Modifier
                .offset(0.dp, (-10).dp)
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
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
                .offset(0.dp, (-6).dp)
        )
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

    LessonCardCompose(viewModel(), lesson)
}

