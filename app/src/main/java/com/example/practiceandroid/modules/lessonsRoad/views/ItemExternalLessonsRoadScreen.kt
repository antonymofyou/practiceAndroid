package com.example.practiceandroid.modules.lessonsRoad.views

import android.app.Application
import android.graphics.Bitmap
import android.media.Image
import android.util.Log
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import com.skydoves.landscapist.rememberDrawablePainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.practiceandroid.R
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadViewModel
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsViewModel


@Composable
fun ItemExternalLessonsRoadScreen(
    modifier: Modifier,
    lessons: ArrayList<Map<String, String>>,
    isScreenLarge: Boolean,
    lessonsRoadViewModel: LessonsRoadViewModel,
    lessonsViewModel: LessonsViewModel,
    previousItemViewType: Int,
    ivLineBottomLeft: MutableState<Boolean>,
    ivLineBottomRight: MutableState<Boolean>,
    ivLineTopLeft: MutableState<Boolean>,
    ivLineTopRight: MutableState<Boolean>,
    clRootMargins: SnapshotStateList<Int>,
    cvChapterBottomBackground: MutableState<Int?>,
    cvChapterTopBackground: MutableState<Int?>,
    tvChapterBottomText: MutableState<String>,
    tvChapterTopText: MutableState<String>,
    isScrollAdapter: Boolean = false,
    // Слушатель нажатия по кружку урока
    onLessonClick: (lesson: Map<String, String>) -> Unit,
    parallaxBackgroundImageFilter: MutableState<ColorFilter?>,
    ivLineTopLeftWidth: MutableState<Int>,
    ivLineTopLeftHeight: MutableState<Int>,
    ivLineTopLeftMargins: SnapshotStateList<Int>,
    ivLineTopRightWidth: MutableState<Int>,
    ivLineTopRightHeight: MutableState<Int>,
    ivLineTopRightMargins: SnapshotStateList<Int>,
    ivLineTopRightRotationX: MutableState<Float>,
    ivLineTopLeftRotationX: MutableState<Float>,
    ivLineBottomLeftWidth: MutableState<Int>,
    ivLineBottomLeftHeight: MutableState<Int>,
    ivLineBottomLeftMargins: SnapshotStateList<Int>,
    ivLineBottomRightWidth: MutableState<Int>,
    ivLineBottomRightHeight: MutableState<Int>,
    ivLineBottomRightMargins: SnapshotStateList<Int>,
    chapterPosition: MutableState<Int?>,
    clRootBackground: MutableState<Color?>,
    clRootMatchParent: MutableState<Boolean>,
    ivLineBottomLeftRes: MutableState<Bitmap?>,
    ivLineBottomRightRes: MutableState<Bitmap?>,
    ivLineTopLeftRes: MutableState<Bitmap?>,
    ivLineTopRightRes: MutableState<Bitmap?>,
    cvChapterBottomConstrainTo: MutableState<Boolean>,
    rvRootWidth: MutableState<Int>,
    scrollYChanged: MutableState<Boolean>
) {

    val parallaxBackgroundImageHeight = remember { mutableStateOf(0) }
    //val parallaxBackgroundImageFilter: MutableState<ColorFilter?> = remember { mutableStateOf(null) }
    val constraints = ConstraintSet {
        val frameLayoutParalaxImage = createRefFor("frameLayoutParalaxImage")
        val parallaxBackgroundImage = createRefFor("parallaxBackgroundImage")
        val ivLineBottomRightConstr = createRefFor("ivLineBottomRight")
        val ivLineBottomLeftConstr = createRefFor("ivLineBottomLeft")
        val ivLineTopLeftConstr = createRefFor("ivLineTopLeft")
        val ivLineTopRightConstr = createRefFor("ivLineTopRight")
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
        constrain(ivLineBottomLeftConstr) {
            bottom.linkTo(parent.bottom)
            start.linkTo(rvRoot.start)
        }
        constrain(ivLineBottomRightConstr) {
            bottom.linkTo(parent.bottom)
            end.linkTo(rvRoot.end)
            top.linkTo(rvRoot.bottom)
        }
        constrain(ivLineTopLeftConstr) {
            bottom.linkTo(rvRoot.top)
            start.linkTo(rvRoot.start)
            top.linkTo(parent.top)
        }
        constrain(ivLineTopRightConstr) {
            bottom.linkTo(rvRoot.top)
            end.linkTo(rvRoot.end)
            top.linkTo(parent.top)
        }
        constrain(cvChapterTop) {
            end.linkTo(parent.end, margin = 16.dp)
            start.linkTo(parent.start, margin = 16.dp)
            top.linkTo(parent.top, margin = 24.dp)
            width = Dimension.fillToConstraints
        }
        constrain(cvChapterBottom) {
            if (cvChapterBottomConstrainTo.value == true) {
                bottom.linkTo(parent.bottom, margin = 24.dp)
            }
            end.linkTo(parent.end, margin = 16.dp)
            start.linkTo(parent.start, margin = 16.dp)
            top.linkTo(rvRoot.bottom, margin = 24.dp)
            width = Dimension.fillToConstraints
        }

    }
    ConstraintLayout(
        modifier = if (clRootBackground.value != null) {
            if (clRootMatchParent.value == false) {
                modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        start = clRootMargins[0].dp,
                        top = clRootMargins[1].dp,
                        end = clRootMargins[2].dp,
                        bottom = clRootMargins[3].dp
                    )
                    .background(clRootBackground.value!!)
                    .onGloballyPositioned { coordinates ->
                        if (lessons.contains(lessonsRoadViewModel.firstUnfulfilledLesson)) {
                            lessonsRoadViewModel.toScrollRvHeight = coordinates.size.height
                        }
                        parallaxBackgroundImageHeight.value = coordinates.size.height
                        chapterPosition.value =
                            lessonsRoadViewModel.getChapterPositionByLessonNumber(
                                lessonsRoadViewModel.firstUnfulfilledLesson!!["lesson_number"].toString(),
                                lessonsRoadViewModel.groupedLessons
                            )
                    }
            } else {
                modifier
                    .fillMaxSize()
                    .padding(
                        start = clRootMargins[0].dp,
                        top = clRootMargins[1].dp,
                        end = clRootMargins[2].dp,
                        bottom = clRootMargins[3].dp
                    )
                    .background(clRootBackground.value!!)
                    .onGloballyPositioned { coordinates ->
                        if (lessons.contains(lessonsRoadViewModel.firstUnfulfilledLesson)) {
                            lessonsRoadViewModel.toScrollRvHeight = coordinates.size.height
                        }
                        chapterPosition.value =
                            lessonsRoadViewModel.getChapterPositionByLessonNumber(
                                lessonsRoadViewModel.firstUnfulfilledLesson!!["lesson_number"].toString(),
                                lessonsRoadViewModel.groupedLessons
                            )
                    }
            }

        } else {
            if (clRootMatchParent.value == false) {
                modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        start = clRootMargins[0].dp,
                        top = clRootMargins[1].dp,
                        end = clRootMargins[2].dp,
                        bottom = clRootMargins[3].dp
                    )
                    .onGloballyPositioned { coordinates ->
                        if (lessons.contains(lessonsRoadViewModel.firstUnfulfilledLesson)) {
                            lessonsRoadViewModel.toScrollRvHeight = coordinates.size.height
                        }
                        chapterPosition.value =
                            lessonsRoadViewModel.getChapterPositionByLessonNumber(
                                lessonsRoadViewModel.firstUnfulfilledLesson!!["lesson_number"].toString(),
                                lessonsRoadViewModel.groupedLessons
                            )
                    }
            } else {
                modifier
                    .fillMaxSize()
                    .padding(
                        start = clRootMargins[0].dp,
                        top = clRootMargins[1].dp,
                        end = clRootMargins[2].dp,
                        bottom = clRootMargins[3].dp
                    )
                    .onGloballyPositioned { coordinates ->
                        if (lessons.contains(lessonsRoadViewModel.firstUnfulfilledLesson)) {
                            lessonsRoadViewModel.toScrollRvHeight = coordinates.size.height
                        }
                        chapterPosition.value =
                            lessonsRoadViewModel.getChapterPositionByLessonNumber(
                                lessonsRoadViewModel.firstUnfulfilledLesson!!["lesson_number"].toString(),
                                lessonsRoadViewModel.groupedLessons
                            )
                    }
            }

        },
        constraintSet = constraints,
    ) {
        Log.d("PARP", "clRoot = ${clRootBackground.value}")
        val heightOfPhone =
            LocalConfiguration.current.screenHeightDp * LocalConfiguration.current.densityDpi / 160
        Box(
            modifier = modifier
                .fillMaxWidth()
                .layoutId("frameLayoutParalaxImage")
        ) {
            val context = LocalContext.current
            Image(
                painter = rememberDrawablePainter(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.lessons_road_bg_repeat
                    )
                ),
                contentDescription = null,
                modifier =
                modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    //.height(parallaxBackgroundImageHeight.value.dp)
                    .layoutId("parallaxBackgroundImage")
                    .alpha(0.8f)
                ,
                contentScale = ContentScale.FillBounds,
                //Возможно будет ошибка и надо было писать modifier = Modifier.graphicsLayer(colorFilter = colorFilter)
                colorFilter = parallaxBackgroundImageFilter.value

            )

        }
        //rvRoot
        Box(
            modifier = modifier
                .width(rvRootWidth.value.dp)
                .wrapContentHeight()
                .layoutId("rvRoot")
                .onGloballyPositioned {
                    lessonsRoadViewModel.scrollY =
                        lessonsRoadViewModel.elementsAfterFirstUnfulfilledLessonHeightSum - lessonsRoadViewModel.toScrollRvHeight + lessonsRoadViewModel.scrollRecyclerHeightSumAfterScrollElement - lessonsViewModel.dpToPx(
                            220
                        ) + if (lessonsViewModel.isScreenLarge) heightOfPhone / 2 else 0
                    scrollYChanged.value = true
                }
        ) {
            Log.d("KIK", "Adapter created")
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
                    .height(ivLineBottomLeftHeight.value.dp)
                    .width(ivLineBottomLeftWidth.value.dp)
                    .layoutId("ivLineBottomLeft")
                    .offset(x = ivLineBottomLeftMargins[0].dp, y = ivLineBottomLeftMargins[3].dp)
                //.padding(start = ivLineBottomLeftMargins[0].dp, top = ivLineBottomLeftMargins[1].dp, end = ivLineBottomLeftMargins[2].dp, bottom = ivLineBottomLeftMargins[3].dp)
                ,
                bitmap = ivLineBottomLeftRes.value!!.asImageBitmap(),
                contentDescription = null
            )
        } else {
            Spacer(
                modifier = modifier
                    .height(ivLineBottomLeftHeight.value.dp)
                    .width(ivLineBottomLeftWidth.value.dp)
                    .layoutId("ivLineBottomLeft")
                    .offset(x = ivLineBottomLeftMargins[0].dp, y = ivLineBottomLeftMargins[3].dp),
                //.padding(start = ivLineBottomLeftMargins[0].dp, top = ivLineBottomLeftMargins[1].dp, end = ivLineBottomLeftMargins[2].dp, bottom = ivLineBottomLeftMargins[3].dp)
            )
        }
        if (ivLineBottomRightRes.value != null) {
            Image(
                bitmap = ivLineBottomRightRes.value!!.asImageBitmap(),
                contentDescription = null,
                modifier = modifier
                    .width(ivLineBottomRightWidth.value.dp)
                    .height(ivLineBottomRightHeight.value.dp)
                    .layoutId("ivLineBottomRight")
                    .offset(x = ivLineBottomRightMargins[0].dp, y = ivLineBottomRightMargins[3].dp)
                //.padding(start = ivLineBottomRightMargins[0].dp, top = ivLineBottomRightMargins[1].dp, end = ivLineBottomRightMargins[2].dp, bottom = ivLineBottomRightMargins[3].dp)
            )
        } else {
            Spacer(
                modifier = modifier
                    .width(ivLineBottomRightWidth.value.dp)
                    .height(ivLineBottomRightHeight.value.dp)
                    .layoutId("ivLineBottomRight")
                    .offset(x = ivLineBottomRightMargins[0].dp, y = ivLineBottomRightMargins[3].dp),
                //.padding(start = ivLineBottomRightMargins[0].dp, top = ivLineBottomRightMargins[1].dp, end = ivLineBottomRightMargins[2].dp, bottom = ivLineBottomRightMargins[3].dp)
            )
        }
        if (ivLineTopLeftRes.value != null) {
            Image(
                modifier = modifier
                    .width(ivLineTopLeftWidth.value.dp)
                    .height(ivLineTopLeftHeight.value.dp)
                    .layoutId("ivLineTopLeft")
                    .graphicsLayer(rotationX = ivLineTopLeftRotationX.value)
                    .offset(x = ivLineTopLeftMargins[0].dp, y = ivLineTopLeftMargins[3].dp),
                //.padding(start = ivLineTopLeftMargins[0].dp, top = ivLineTopLeftMargins[1].dp, end = ivLineTopLeftMargins[2].dp, bottom = ivLineTopLeftMargins[3].dp),
                bitmap = ivLineTopLeftRes.value!!.asImageBitmap(),
                contentDescription = null,
            )
        } else {
            Spacer(
                modifier = modifier
                    .width(ivLineTopLeftWidth.value.dp)
                    .height(ivLineTopLeftHeight.value.dp)
                    .layoutId("ivLineTopLeft")
                    .graphicsLayer(rotationX = ivLineTopLeftRotationX.value)
                    .offset(x = ivLineTopLeftMargins[0].dp, y = ivLineTopLeftMargins[3].dp)
                //.padding(start = ivLineTopLeftMargins[0].dp, top = ivLineTopLeftMargins[1].dp, end = ivLineTopLeftMargins[2].dp, bottom = ivLineTopLeftMargins[3].dp),
            )
        }
        //надо сделать инвизы
        if (ivLineTopRightRes.value != null) {
            Image(
                modifier = modifier
                    .width(ivLineTopRightWidth.value.dp)
                    .height(ivLineTopRightHeight.value.dp)
                    .layoutId("ivLineTopRight")
                    .graphicsLayer(rotationX = ivLineTopRightRotationX.value)
                    .offset(x = ivLineTopRightMargins[0].dp, y = ivLineTopRightMargins[3].dp),
                //.padding(start = ivLineTopRightMargins[0].dp, top = ivLineTopRightMargins[1].dp, end = ivLineTopRightMargins[2].dp, bottom = ivLineTopRightMargins[3].dp),
                bitmap = ivLineTopRightRes.value!!.asImageBitmap(),
                contentDescription = null
            )
        } else {
            Spacer(
                modifier = modifier
                    .width(ivLineTopRightWidth.value.dp)
                    .height(ivLineTopRightHeight.value.dp)
                    .layoutId("ivLineTopRight")
                    .graphicsLayer(rotationX = ivLineTopRightRotationX.value)
                    .offset(x = ivLineTopRightMargins[0].dp, y = ivLineTopRightMargins[3].dp),
                //.padding(start = ivLineTopRightMargins[0].dp, top = ivLineTopRightMargins[1].dp, end = ivLineTopRightMargins[2].dp, bottom = ivLineTopRightMargins[3].dp),
            )
        }
        Card(
            modifier =
            if (cvChapterTopBackground.value != null) {
                Modifier
                    .height(50.dp)
                    .layoutId("cvChapterTop")
                    .background(Color(cvChapterTopBackground.value!!))
            } else {
                Modifier
                    .height(50.dp)
                    .layoutId("cvChapterTop")
            },
            shape = RoundedCornerShape(25.dp),
            elevation = CardDefaults.cardElevation(0.dp),
            colors = CardDefaults.cardColors(colorResource(R.color.lesson_surface_color))

        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = tvChapterTopText.value,
                    modifier = Modifier
                        .wrapContentSize(),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    color = colorResource(R.color.lesson_text_color),
                    textAlign = TextAlign.Center,
                )
            }
        }
        Card (
            modifier = if (cvChapterBottomBackground.value != null) {
                modifier
                    .layoutId("cvChapterBottom")
                    .background(Color(cvChapterBottomBackground.value!!))
                    .height(50.dp)
            } else {
                modifier
                    .layoutId("cvChapterBottom")
                    .height(50.dp)
            },
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(colorResource(R.color.lesson_surface_color))
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = modifier
                        .wrapContentSize(),
                    text = tvChapterBottomText.value,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    fontSize = 20.sp,
                    color = colorResource(R.color.lesson_text_color)
                )
            }
        }


    }

}

@Preview
@Composable
fun ItemExternalLessonsRoadScreenPreview() {
    var lessonsViewModel = LessonsViewModel(LocalContext.current.applicationContext as Application)
    var lessonsRoadViewModel =
        LessonsRoadViewModel(LocalContext.current.applicationContext as Application)
    val verticalScroll = rememberScrollState()
    val loadingLayout = remember { mutableStateOf(false) }
    val tvErrorLessonsRoadList = remember { mutableStateOf(false) }
    val tvErrorLessonsRoadListText = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val ivLineTopLeftWidth = remember { mutableStateOf(100) }
    val ivLineTopLeftHeight = remember { mutableStateOf(110) }
    val ivLineTopLeftMargins = remember { mutableStateListOf(84, (-10), 0, 0) }
    val ivLineTopRightWidth = remember { mutableStateOf(100) }
    val ivLineTopRightHeight = remember { mutableStateOf(105) }
    val ivLineTopRightMargins = remember { mutableStateListOf(0, 0, 90, 0) }
    val ivLineBottomLeftWidth = remember { mutableStateOf(90) }
    val ivLineBottomLeftHeight = remember { mutableStateOf(105) }
    val ivLineBottomLeftMargins = remember { mutableStateListOf(94, 0, 0, (-6)) }
    val ivLineBottomLeftRes: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    val ivLineBottomRightWidth = remember { mutableStateOf(100) }
    val ivLineBottomRightHeight = remember { mutableStateOf(112) }
    val ivLineBottomRightMargins = remember { mutableStateListOf(0, 0, 92, (-4)) }
    val ivLineBottomRightRes: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    val uppButtonLayout = remember { mutableStateOf(true) }
    val ivLineBottomLeft: MutableState<Boolean> = remember { mutableStateOf(false) }
    val ivLineBottomRight: MutableState<Boolean> = remember { mutableStateOf(false) }
    val ivLineTopLeft: MutableState<Boolean> = remember { mutableStateOf(false) }
    val ivLineTopRight: MutableState<Boolean> = remember { mutableStateOf(false) }
    val ivLineBottomRightRotationX = remember { mutableStateOf(0f) }
    val clRootHeight = remember { mutableStateOf(0) }
    val clRootMargins = remember { mutableStateListOf(0, 0, 0, 0) }
    val coroutineScope = rememberCoroutineScope()
    val clRootBackground: MutableState<Color?> = remember { mutableStateOf(null) }
    val lessonsRoadListStatus =
        remember { mutableStateOf(lessonsRoadViewModel.lessonsRoadListStatus.value) }
    val chapterPosition: MutableState<Int?> = remember { mutableStateOf(null) }
    val cvChapterBottomBackground: MutableState<Int?> = remember { mutableStateOf(null) }
    val cvChapterTopBackground: MutableState<Int?> = remember { mutableStateOf(null) }
    val tvChapterBottomText = remember { mutableStateOf("Человек и общество") }
    val tvChapterTopText = remember { mutableStateOf("Человек и общество") }
    val clRootMatchParent = remember { mutableStateOf(false) }
    val ivLineTopRightRotationX = remember { mutableStateOf(0f) }
    val ivLineTopLeftRotationX = remember { mutableStateOf(0f) }
    ///нужно ли привязывать bottom cvChapter к parent
    val cvChapterBottomConstrainTo = remember { mutableStateOf(true) }
    val ivLineTopRightRes: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    val ivLineTopLeftRes: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    val rvRootWidth = remember { mutableStateOf(365) }
    val scrollYChanged = remember { mutableStateOf(false) }
    val roadlist = arrayListOf<Map<String, String>>()
    if (lessonsRoadViewModel.lessonsRoadList != null) {
        // Установка списка дорожки уроков
        for (road in lessonsRoadViewModel.lessonsRoadList!!) {
            roadlist.add(road)
        }
        lessonsRoadViewModel.groupedLessons =
            lessonsRoadViewModel.getLessonsByChapter(roadlist)

        // В зависимости от ширины экрана устанавливаем размеры дорожки уроков
        lessonsRoadViewModel.groupedLessons[0]
        var parallaxBackgroundImageFilter: MutableState<ColorFilter?> =
            remember { mutableStateOf(null) }
        val color = lessonsRoadViewModel.getParallaxImageColorForChapter(
            lessonsRoadViewModel.groupedLessons[0][0]["lesson_chapter"] ?: ""
        )
        val colorFilter = ColorFilter.tint(Color(color), BlendMode.SrcIn)
        parallaxBackgroundImageFilter.value = colorFilter
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                ItemExternalLessonsRoadScreen(
                    modifier = Modifier,
                    lessons = lessonsRoadViewModel.groupedLessons[0],
                    isScreenLarge = false,
                    lessonsRoadViewModel = lessonsRoadViewModel,
                    lessonsViewModel = lessonsViewModel,
                    previousItemViewType = 1,
                    ivLineBottomLeft = ivLineBottomLeft,
                    ivLineBottomRight = ivLineBottomRight,
                    ivLineTopLeft = ivLineTopLeft,
                    ivLineTopRight = ivLineTopRight,
                    clRootMargins = clRootMargins,
                    cvChapterBottomBackground = cvChapterBottomBackground,
                    cvChapterTopBackground = cvChapterTopBackground,
                    tvChapterBottomText = tvChapterBottomText,
                    tvChapterTopText = tvChapterTopText,
                    isScrollAdapter = false,
                    // Слушатель нажатия по кружку урока
                    onLessonClick = {},
                    parallaxBackgroundImageFilter = parallaxBackgroundImageFilter,
                    ivLineTopLeftWidth = ivLineTopLeftWidth,
                    ivLineTopLeftHeight = ivLineTopLeftHeight,
                    ivLineTopLeftMargins = ivLineTopLeftMargins,
                    ivLineTopRightWidth = ivLineTopRightWidth,
                    ivLineTopRightHeight = ivLineTopRightHeight,
                    ivLineTopRightMargins = ivLineTopRightMargins,
                    ivLineTopRightRotationX = ivLineTopRightRotationX,
                    ivLineTopLeftRotationX = ivLineTopLeftRotationX,
                    ivLineBottomLeftWidth = ivLineBottomLeftWidth,
                    ivLineBottomLeftHeight = ivLineBottomLeftHeight,
                    ivLineBottomLeftMargins = ivLineBottomLeftMargins,
                    ivLineBottomRightWidth = ivLineBottomRightWidth,
                    ivLineBottomRightHeight = ivLineBottomRightHeight,
                    ivLineBottomRightMargins = ivLineBottomRightMargins,
                    chapterPosition = chapterPosition,
                    clRootBackground = clRootBackground,
                    clRootMatchParent = clRootMatchParent,
                    ivLineBottomLeftRes = ivLineBottomLeftRes,
                    ivLineBottomRightRes = ivLineBottomRightRes,
                    ivLineTopLeftRes = ivLineTopLeftRes,
                    ivLineTopRightRes = ivLineTopRightRes,
                    cvChapterBottomConstrainTo = cvChapterBottomConstrainTo,
                    rvRootWidth = rvRootWidth,
                    scrollYChanged = scrollYChanged
                )
            }
        }
    }
}