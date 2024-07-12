package com.example.practiceandroid.modules.lessonsRoad.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.practiceandroid.R
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadViewModel
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsViewModel

@Composable
fun ItemExternalLessonsRoadScreenHeight(
    modifier: Modifier,
    lessons: ArrayList<Map<String, String>>,
    isScreenLarge: Boolean,
    lessonsRoadViewModel: LessonsRoadViewModel,
    lessonsViewModel: LessonsViewModel,
    previousItemViewType: Int,
    isScrollAdapter: Boolean = false,
    // Слушатель нажатия по кружку урока
    onLessonClick: (lesson: Map<String, String>) -> Unit,
    parallaxBackgroundImageFilter: MutableState<ColorFilter?>,
    getHeight: (Int) -> Unit
) {

    val ivLineBottomLeftRes: MutableState<Int?> = remember { mutableStateOf(null) }
    val ivLineBottomRightRes: MutableState<Int?> = remember { mutableStateOf(null) }
    val ivLineTopLeftRes: MutableState<Int?> = remember { mutableStateOf(null) }
    val ivLineTopRightRes: MutableState<Int?> = remember { mutableStateOf(null) }
    //val parallaxBackgroundImageFilter: MutableState<ColorFilter?> = remember { mutableStateOf(null) }
    val constraints = ConstraintSet {
        val frameLayoutParalaxImage = createRefFor("frameLayoutParalaxImage")
        val parallaxBackgroundImage = createRefFor("parallaxBackgroundImage")
        val ivLineBottomRight = createRefFor("ivLineBottomRight")
        val ivLineBottomLeft = createRefFor("ivLineBottomLeft")
        val ivLineTopLeft = createRefFor("ivLineBottomLeft")
        val ivLineTopRight = createRefFor("ivLineBottomRight")
        val rvRoot = createRefFor("rvRoot")
        val cvChapterTop = createRefFor("cvChapterTop")
        val cvChapterBottom = createRefFor("cvChapterBottom")
        constrain(frameLayoutParalaxImage) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            height = Dimension.fillToConstraints
        }
        constrain(rvRoot) {
            end.linkTo(parent.end)
            start.linkTo(parent.start)
            top.linkTo(cvChapterTop.bottom, margin = 24.dp)
        }
        constrain(ivLineBottomLeft) {
            bottom.linkTo(parent.bottom)
            start.linkTo(rvRoot.start)
        }
        constrain(ivLineBottomRight) {
            bottom.linkTo(parent.bottom)
            end.linkTo(rvRoot.end)
            top.linkTo(rvRoot.bottom)
        }
        constrain(ivLineTopLeft) {
            bottom.linkTo(rvRoot.top)
            start.linkTo(rvRoot.start)
            top.linkTo(parent.top)
        }
        constrain(ivLineTopRight) {
            bottom.linkTo(rvRoot.top)
            end.linkTo(rvRoot.end)
            top.linkTo(parent.top)
        }
        constrain(cvChapterTop) {
            end.linkTo(parent.end)
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            height = Dimension.fillToConstraints
        }
        constrain(cvChapterBottom) {
            bottom.linkTo(parent.bottom, margin = 24.dp)
            end.linkTo(parent.end, margin = 16.dp)
            start.linkTo(parent.start, margin = 16.dp)
            top.linkTo(rvRoot.bottom, margin = 24.dp)
            width = Dimension.fillToConstraints
        }

    }
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .onGloballyPositioned { coordinates ->
                getHeight(coordinates.size.height)
            },
        constraintSet = constraints
    ) {

        Box(
            modifier = modifier
                .fillMaxWidth()
                .layoutId("frameLayoutParalaxImage")
        ) {
            Image(
                painter = painterResource(R.drawable.lessons_road_bg_repeat),
                contentDescription = null,
                modifier =
                modifier
                    .fillMaxWidth()
                    .height(0.dp)
                    .layoutId("parallaxBackgroundImage")
                    .alpha(0.8f),
                contentScale = ContentScale.FillBounds,
                //Возможно будет ошибка и надо было писать modifier = Modifier.graphicsLayer(colorFilter = colorFilter)
                colorFilter = parallaxBackgroundImageFilter.value

            )

        }
        //rvRoot
        Box(
            modifier = modifier
                .width(365.dp)
                .wrapContentHeight()
                .layoutId("rvRoot")
        ) {
            LessonsRoadAdapterScreen(
                modifier = modifier,
                lessons = lessons,
                isScreenLarge = isScreenLarge,
                lessonsRoadViewModel = lessonsRoadViewModel,
                lessonsViewModel = lessonsViewModel,
                previousItemViewType = previousItemViewType,
                isScrollAdapter = isScrollAdapter,
                // Слушатель нажатия по кружку урока
                onLessonClick = onLessonClick,
            )
        }
        if (ivLineBottomLeftRes.value != null) {
            Image(
                modifier = modifier
                    .height(105.dp)
                    .width(90.dp)
                    .layoutId("ivLineBottomLeft")
                    .padding(start = 94.dp, bottom = (-6).dp),
                painter = painterResource(ivLineBottomLeftRes.value!!),
                contentDescription = null
            )
        }
        if (ivLineBottomRightRes.value != null) {
            Image(
                painter = painterResource(ivLineBottomRightRes.value!!),
                contentDescription = null,
                modifier = modifier
                    .width(100.dp)
                    .height(112.dp)
                    .layoutId("ivLineBottomRight")
                    .padding(end = 92.dp, bottom = (-4).dp)
            )
        }
        if (ivLineTopLeftRes.value != null) {
            Image(
                modifier = modifier
                    .width(100.dp)
                    .height(110.dp)
                    .padding(start = 84.dp, top = (-10).dp),
                painter = painterResource(ivLineTopLeftRes.value!!),
                contentDescription = null
            )
        }
        if (ivLineTopRightRes.value != null) {
            Image(
                modifier = modifier
                    .width(100.dp)
                    .height(105.dp)
                    .padding(end = 90.dp),
                painter = painterResource(ivLineTopRightRes.value!!),
                contentDescription = null
            )
        }
        Card(
            modifier = modifier
                .height(50.dp)
                .layoutId("cvChapterTop")
                .padding(top = 24.dp, start = 16.dp, end = 16.dp),
            shape = RoundedCornerShape(25.dp),
            elevation = CardDefaults.cardElevation(0.dp),
            colors = CardDefaults.cardColors(colorResource(R.color.lesson_surface_color))

        ) {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Человек и общество",
                    modifier = modifier
                        .fillMaxSize(),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    color = colorResource(R.color.lesson_text_color),
                    textAlign = TextAlign.Center,
                )
            }
        }
        Card(
            modifier = modifier
                .layoutId("cvChapterBottom")
                .height(50.dp),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(colorResource(R.color.lesson_surface_color))
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    modifier = modifier
                        .fillMaxSize(),
                    text = "Человек и общество",
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    fontSize = 20.sp,
                    color = colorResource(R.color.lesson_text_color)
                )
            }
        }
    }

}