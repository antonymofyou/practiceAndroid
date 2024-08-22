package com.example.practiceandroid.modules.lessonsRoad.views

import android.graphics.Bitmap
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.practiceandroid.ConfigData
import com.example.practiceandroid.R
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadViewModel

/**
 * Функция, отвечающая за отображение кружка урока
 *
 * @param lessonsRoadViewModel
 * @param lesson: информация о уроке
 */
@Composable
fun LessonCardLeftCompose(
    lessonsRoadViewModel: LessonsRoadViewModel,
    lesson: Map<String, String>,
    marginTop: Dp = 0.dp,
    lineBitmap: Bitmap? = null,
    rotationY: Float = 0f,
    isLastIndex: Boolean = true
) {

    val statusText = lesson["status"]?.let {
        lessonsRoadViewModel.setLessonStatusNameById(it)
    } ?: "Готово"

    val statusColor = lesson["status"]?.let {
        Color(lessonsRoadViewModel.getLessonsStatusColorById(it))
    } ?: Color(0xFF4CAF50)

    Box (
        modifier = Modifier
            .offset(0.dp, marginTop)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(0.dp, 0.dp, 25.dp, 0.dp)
        ) {
            Column (
                modifier = if (isLastIndex) Modifier.wrapContentSize() else Modifier
                    .wrapContentWidth()
                    .height(240.dp)
            ) {
                if (lineBitmap != null) {
                    Image(
                        bitmap = lineBitmap.asImageBitmap(),
                        contentDescription = "Line between lessons",
                        alignment = Alignment.TopCenter,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(70.dp)
                            .height(60.dp)
                            .padding(10.dp, 0.dp, 0.dp, 0.dp)
                            .rotate(rotationY)
                    )
                }

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

    LessonCardLeftCompose(viewModel(), lesson)
}

