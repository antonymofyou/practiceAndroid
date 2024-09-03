package com.example.practiceandroid.modules.lessonsRoad.views

import android.graphics.Paint
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
import android.graphics.Path
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
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
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsViewModel

/**
 * Класс, отвечающий за представление кружков занятий.
 */
class LessonCardView(
    private val lessonsRoadViewModel: LessonsRoadViewModel,
    private val lessonsViewModel: LessonsViewModel,
    private val screenWidthDp: Int
    ) {

    // Параметры соединяющих линий
    private val leftUpperLineHeight = 110
    private val rightUpperLineHeight = 96
    private val leftLowerLineHeight = 110
    private val rightLowerLineHeight = 105

    private val leftUpperLineWidth = screenWidthDp / 2 - 90
    private val rightUpperLineWidth = screenWidthDp / 2 - 105
    private val leftLowerLineWidth = leftLowerLineHeight * rightUpperLineWidth / rightUpperLineHeight
    private val rightLowerLineWidth = rightLowerLineHeight * leftUpperLineWidth / leftUpperLineHeight

    /**
     * Функция, отвечающая за отображение кружка урока,
     * когда первый урок находится слева
     *
     * @param lesson: информация о уроке
     * @param position: порядковый номер урока в списке всех занятий
     * @param isFirstLessonInChapter: указывает, если урок первый в списке занятий из раздела
     * @param isLastLessonInChapter: указывает, если урок последний в списке занятий из раздела
     * @param lastIndex: номер самого последнего урока
     */
    @Composable
    fun LessonCardLeftCompose(lesson: Map<String, String>, position: Int, isFirstLessonInChapter: Boolean, isLastLessonInChapter: Boolean, lastIndex: Int) {

        // Получение параметров для отображения информации о занятии
        val statusText = lesson["status"]?.let {
            lessonsRoadViewModel.setLessonStatusNameById(it)
        } ?: "Готово"

        val statusColor = lesson["status"]?.let {
            Color(lessonsRoadViewModel.getLessonsStatusColorById(it))
        } ?: Color(0xFF4CAF50)

        // Настройка смещения влево/вправо, вверх/вниз и высоты
        var boxModifier = Modifier.fillMaxWidth()
        var paddingTop = 0.dp
        var paddingBottom = 0.dp
        if (isFirstLessonInChapter && isLastLessonInChapter) {
            boxModifier = boxModifier
                .wrapContentHeight()
                .padding(50.dp, 0.dp, 0.dp, 0.dp)
            paddingTop = 98.dp
            paddingBottom = 98.dp
        } else if (!isFirstLessonInChapter && isLastLessonInChapter) {
            boxModifier = boxModifier
                .wrapContentHeight()
                .padding(50.dp, 0.dp, 0.dp, 0.dp)
            paddingBottom = 98.dp
        } else if (isFirstLessonInChapter && !isLastLessonInChapter) {
            boxModifier = boxModifier
                .wrapContentHeight()
                .padding(50.dp, 0.dp, 0.dp, 0.dp)
            paddingTop = 98.dp
        } else if (position % 2 == 0) {
            boxModifier = boxModifier
                .wrapContentHeight()
                .padding(50.dp, 0.dp, 0.dp, 0.dp)
        } else {
            boxModifier = boxModifier
                .height(240.dp)
                .padding(50.dp, 0.dp, 0.dp, 0.dp)
        }

        Box(
            contentAlignment = Alignment.TopStart,
            modifier = boxModifier
        ) {

            // Верхняя линия
            if (isFirstLessonInChapter && position != 0) {
                val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = lessonsRoadViewModel.getLineColorForChapter(lesson["lesson_chapter"]!!)
                }
                val circlePath = Path()
                val lineToRight = lessonsRoadViewModel.createLineBitmapLeftToRight(
                    path = circlePath,
                    paint = paint,
                    width = lessonsViewModel.dpToPx(leftUpperLineWidth),
                    height = lessonsViewModel.dpToPx(leftUpperLineHeight)
                )
                Box (
                    contentAlignment = Alignment.TopEnd,
                    modifier = Modifier
                        .width((screenWidthDp / 2 - 50).dp)
                        .height(110.dp)
                ) {
                    Image(
                        bitmap = lineToRight.asImageBitmap(),
                        contentDescription = "Right line",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .graphicsLayer {
                                rotationY = 180f
                            }
                    )
                }
            }

            // Высота кружка и подписей под ним
            var columnHeight by remember {
                mutableStateOf(0.dp)
            }

            // Кружок и подписи под ним
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .width(130.dp)
                    .wrapContentHeight()
                    .padding(0.dp, paddingTop, 0.dp, paddingBottom)
                    .onGloballyPositioned { coordinates ->
                        columnHeight = lessonsViewModel.pxToDp(coordinates.size.height).dp
                    }
            ) {
                // Загрузка изображения для кружка
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

                // Текст о состоянии занятия
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

                // Название занятия
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

            // Нижняя линия
            if (position != lastIndex) {
                if (isLastLessonInChapter) {
                    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = lessonsRoadViewModel.getLineColorForChapter(lesson["lesson_chapter"]!!)
                    }
                    val circlePath = Path()
                    val lineToRight = lessonsRoadViewModel.createLineBitmapLeftToRight(
                        path = circlePath,
                        paint = paint,
                        width = lessonsViewModel.dpToPx(leftLowerLineWidth),
                        height = lessonsViewModel.dpToPx(leftLowerLineHeight)
                    )
                    Box (
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier
                            .width((screenWidthDp / 2 - 50).dp)
                            .height(columnHeight + 98.dp + if (isFirstLessonInChapter && position != 0) 98.dp else 0.dp)
                    ) {
                        Image(
                            bitmap = lineToRight.asImageBitmap(),
                            contentDescription = "Right line",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .rotate(180f)
                        )
                    }
                }
//            else {
//                TODO("Side line")
//            }
            }
        }
    }

    /**
     * Функция, отвечающая за отображение кружка урока,
     * когда первый урок находится справа
     *
     * @param lesson: информация о уроке
     * @param position: порядковый номер урока в списке всех занятий
     * @param isFirstLessonInChapter: указывает, если урок первый в списке занятий из раздела
     * @param isLastLessonInChapter: указывает, если урок последний в списке занятий из раздела
     * @param lastIndex: номер самого последнего урока
     */
    @Composable
    fun LessonCardRightCompose(lesson: Map<String, String>, position: Int, isFirstLessonInChapter: Boolean, isLastLessonInChapter: Boolean, lastIndex: Int) {

        // Получение параметров для отображения информации о занятии
        val statusText = lesson["status"]?.let {
            lessonsRoadViewModel.setLessonStatusNameById(it)
        } ?: "Готово"

        val statusColor = lesson["status"]?.let {
            Color(lessonsRoadViewModel.getLessonsStatusColorById(it))
        } ?: Color(0xFF4CAF50)

        // Настройка смещения влево/вправо, вверх/вниз и высоты
        var boxModifier = Modifier.fillMaxWidth()
        var paddingTop = 0.dp
        var paddingBottom = 0.dp
        if (isFirstLessonInChapter && isLastLessonInChapter) {
            boxModifier = boxModifier
                .wrapContentHeight()
                .padding(0.dp, 0.dp, 50.dp, 0.dp)
            paddingTop = 98.dp
            paddingBottom = 98.dp
        } else if (!isFirstLessonInChapter && isLastLessonInChapter) {
            boxModifier = boxModifier
                .wrapContentHeight()
                .padding(0.dp, 0.dp, 50.dp, 0.dp)
            paddingBottom = 98.dp
        } else if (isFirstLessonInChapter && !isLastLessonInChapter) {
            boxModifier = boxModifier
                .wrapContentHeight()
                .padding(0.dp, 0.dp, 50.dp, 0.dp)
            paddingTop = 98.dp
        } else if (position % 2 == 0) {
            boxModifier = boxModifier
                .wrapContentHeight()
                .padding(0.dp, 0.dp, 50.dp, 0.dp)
        } else {
            boxModifier = boxModifier
                .height(240.dp)
                .padding(0.dp, 0.dp, 50.dp, 0.dp)
        }

        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = boxModifier
        ) {

            // Верхняя линия
            if (isFirstLessonInChapter && position != 0) {
                val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = lessonsRoadViewModel.getLineColorForChapter(lesson["lesson_chapter"]!!)
                }
                val circlePath = Path()
                val lineToLeft = lessonsRoadViewModel.createLineBitmapRightToLeft(
                    path = circlePath,
                    paint = paint,
                    width = lessonsViewModel.dpToPx(rightUpperLineWidth),
                    height = lessonsViewModel.dpToPx(rightUpperLineHeight)
                )
                Box (
                    contentAlignment = Alignment.TopStart,
                    modifier = Modifier
                        .width((screenWidthDp / 2 - 50).dp)
                        .height(100.dp)
                ) {
                    Image(
                        bitmap = lineToLeft.asImageBitmap(),
                        contentDescription = "Right line",
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Высота кружка и подписей под ним
            var columnHeight by remember {
                mutableStateOf(0.dp)
            }

            // Кружок и подписи под ним
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .width(130.dp)
                    .wrapContentHeight()
                    .padding(0.dp, paddingTop, 0.dp, paddingBottom)
                    .onGloballyPositioned { coordinates ->
                        columnHeight = lessonsViewModel.pxToDp(coordinates.size.height).dp
                    }
            ) {

                // Загрузка изображения для кружка
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

                // Текст о состоянии занятия
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

                // Название занятия
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

            // Нижняя линия
            if (position != lastIndex) {
                if (isLastLessonInChapter) {
                    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = lessonsRoadViewModel.getLineColorForChapter(lesson["lesson_chapter"]!!)
                    }
                    val circlePath = Path()
                    val lineToRight = lessonsRoadViewModel.createLineBitmapRightToLeft(
                        path = circlePath,
                        paint = paint,
                        width = lessonsViewModel.dpToPx(rightLowerLineWidth),
                        height = lessonsViewModel.dpToPx(rightLowerLineHeight)
                    )
                    Box (
                        contentAlignment = Alignment.BottomStart,
                        modifier = Modifier
                            .width((screenWidthDp / 2 - 50).dp)
                            .height(
                                columnHeight + 98.dp + if (isFirstLessonInChapter && position != 0) 98.dp else 0.dp
                            )
                    ) {
                        Image(
                            bitmap = lineToRight.asImageBitmap(),
                            contentDescription = "Right line",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .graphicsLayer {
                                    rotationX = 180f
                                }
                        )
                    }
                }
//            else {
//                TODO("Side line")
//            }
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

    val screenWidthDp = with(LocalConfiguration.current) { screenWidthDp }
    val lessonCardView = LessonCardView(viewModel(), viewModel(), screenWidthDp)

    lessonCardView.LessonCardLeftCompose(lesson, 0, isFirstLessonInChapter = true, isLastLessonInChapter = true, 0)
}

