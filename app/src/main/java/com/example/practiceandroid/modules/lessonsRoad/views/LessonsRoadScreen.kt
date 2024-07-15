package com.example.practiceandroid.modules.lessonsRoad.views

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import android.widget.LinearLayout.LayoutParams
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practiceandroid.R
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonStatus
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadListStatus
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadViewModel
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsViewModel
import kotlinx.coroutines.launch

val circlePath = Path()

var topLeftBitmap: Bitmap? = null
var topRightBitmap: Bitmap? = null
var bottomLeftBitmap: Bitmap? = null
var bottomRightBitmap: Bitmap? = null
@Composable
fun LessonsRoadScreen(modifier: Modifier) {
    // Объявляем ViewModel
    val lessonsRoadViewModel: LessonsRoadViewModel = viewModel()
    val lessonsViewModel: LessonsViewModel = viewModel()
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.parseColor("#27A4FF")
    }

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
    val clRootBackground: MutableState<Int?> = remember { mutableStateOf(null) }
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
    //val rvRootHeight = remember { mutableStateOf() }


    //lessonsRoadViewModel.lessonsRoadListStatus.observeAsState()
    if (lessonsRoadListStatus.value != null) {
        loadingLayout.value = false
        tvErrorLessonsRoadList.value = false
        uppButtonLayout.value = false
        when (lessonsRoadListStatus.value) {
            LessonsRoadListStatus.LOADING -> {
                loadingLayout.value = false
            }

            LessonsRoadListStatus.UNLOG -> {

            }

            LessonsRoadListStatus.ERROR -> {
                tvErrorLessonsRoadList.value = true
                tvErrorLessonsRoadListText.value = lessonsRoadViewModel.errMess
            }

            LessonsRoadListStatus.SUCCESS -> {
                uppButtonLayout.value = true
                val roadlist = arrayListOf<Map<String, String>>()
                if (lessonsRoadViewModel.lessonsRoadList != null) {
                    // Установка списка дорожки уроков
                    for (road in lessonsRoadViewModel.lessonsRoadList!!) {
                        roadlist.add(road)
                    }
                    lessonsRoadViewModel.groupedLessons =
                        lessonsRoadViewModel.getLessonsByChapter(roadlist)
                    // В зависимости от ширины экрана устанавливаем размеры дорожки уроков
                    lessonsViewModel.isScreenLarge =
                        lessonsViewModel.pxToDp(LocalContext.current.resources.displayMetrics.widthPixels) >= 730
                    lessonsRoadViewModel.groupedLessons.forEachIndexed { index, lessons ->
                        var modifier = Modifier
                        var lessons = lessons
                        var isScreenLarge = lessonsViewModel.isScreenLarge
                        var lessonsRoadViewModel = lessonsRoadViewModel
                        var lessonsViewModel = lessonsViewModel
                        var previousItemViewType =
                            lessonsRoadViewModel.getGroupedLessonsWithViewType(
                                lessonsRoadViewModel.groupedLessons
                            )[index].viewType
                        var isScrollAdapter =
                            lessons.contains(lessonsRoadViewModel.firstUnfulfilledLesson)
                        var onLessonClick: (lesson: Map<String, String>) -> Unit = { lesson ->
                            if (lesson["status"] != "0") {
                                lessonsViewModel.currentLesson.value = lesson
                                lessonsViewModel.lessonStatus.value =
                                    LessonStatus.LESSON
                                lessonsViewModel.currentLessonNum.value =
                                    lesson["lesson_number"]
                            }
                        }
                        var parallaxBackgroundImageFilter: MutableState<ColorFilter?> =
                            remember { mutableStateOf(null) }
                        val color = lessonsRoadViewModel.getParallaxImageColorForChapter(
                            lessons[0]["lesson_chapter"] ?: ""
                        )
                        val colorFilter = ColorFilter.tint(Color(color), BlendMode.SrcIn)
                        parallaxBackgroundImageFilter.value = colorFilter
                        //тут проблема пост делается когда view уже создано
                        ItemExternalLessonsRoadScreen(
                            modifier = modifier,
                            lessons = lessons,
                            isScreenLarge = isScreenLarge,
                            lessonsRoadViewModel = lessonsRoadViewModel,
                            lessonsViewModel = lessonsViewModel,
                            previousItemViewType = previousItemViewType,
                            ivLineBottomLeft = ivLineBottomLeft,
                            ivLineBottomRight = ivLineBottomRight,
                            ivLineTopLeft = ivLineTopLeft,
                            ivLineTopRight = ivLineTopRight,
                            clRootBackground = clRootBackground,
                            clRootMargins = clRootMargins,
                            cvChapterBottomBackground = cvChapterBottomBackground,
                            cvChapterTopBackground = cvChapterTopBackground,
                            tvChapterBottomText = tvChapterBottomText,
                            tvChapterTopText = tvChapterTopText,
                            isScrollAdapter = isScrollAdapter,
                            chapterPosition = chapterPosition,
                            // Слушатель нажатия по кружку урока
                            onLessonClick = { lesson ->
                                if (lesson["status"] != "0") {
                                    lessonsViewModel.currentLesson.value = lesson
                                    lessonsViewModel.lessonStatus.value =
                                        LessonStatus.LESSON
                                    lessonsViewModel.currentLessonNum.value =
                                        lesson["lesson_number"]
                                }
                            },
                            parallaxBackgroundImageFilter = parallaxBackgroundImageFilter,
                            ivLineTopLeftWidth = ivLineTopLeftWidth,
                            ivLineTopLeftHeight = ivLineTopLeftHeight,
                            ivLineTopLeftMargins = ivLineTopLeftMargins,
                            ivLineTopRightWidth = ivLineTopRightWidth,
                            ivLineTopRightHeight = ivLineTopRightHeight,
                            ivLineTopRightMargins = ivLineTopRightMargins,
                            ivLineBottomLeftWidth = ivLineBottomLeftWidth,
                            ivLineBottomLeftHeight = ivLineBottomLeftHeight,
                            ivLineBottomLeftMargins = ivLineBottomLeftMargins,
                            ivLineBottomRightWidth = ivLineBottomRightWidth,
                            ivLineBottomRightHeight = ivLineBottomRightHeight,
                            ivLineBottomRightMargins = ivLineBottomRightMargins,
                            ivLineTopRightRotationX = ivLineTopRightRotationX,
                            ivLineTopLeftRotationX = ivLineTopLeftRotationX,
                            clRootMatchParent = clRootMatchParent,
                            ivLineBottomLeftRes = ivLineBottomLeftRes,
                            ivLineBottomRightRes = ivLineBottomRightRes,
                            ivLineTopLeftRes = ivLineTopLeftRes,
                            ivLineTopRightRes = ivLineTopRightRes,
                            cvChapterBottomConstrainTo = cvChapterBottomConstrainTo,
                            scrollYChanged = scrollYChanged,
                            rvRootWidth = rvRootWidth
                        )
                        if (lessons.contains(lessonsRoadViewModel.firstUnfulfilledLesson)) {
                            lessonsRoadViewModel.scrollToIndex = index + 2
                            lessonsRoadViewModel.scrollToLesson =
                                lessonsRoadViewModel.firstUnfulfilledLesson!!
                            lessonsRoadViewModel.scrollToLessons = lessons
                        }
                        //опять пост
                        if (chapterPosition.value != null) {
                            if (index <= chapterPosition.value!!) {
                                lessonsRoadViewModel.elementsAfterFirstUnfulfilledLessonHeightSum += lessonsRoadViewModel.toScrollRvHeight
                            }
                        }
                        ivLineBottomLeft.value = false
                        ivLineBottomRight.value = false
                        ivLineTopLeft.value = false
                        ivLineTopRight.value = false

                        val groupedLessonsWithViewType =
                            lessonsRoadViewModel.getGroupedLessonsWithViewType(
                                lessonsRoadViewModel.groupedLessons
                            )

                        val chapterLessons = groupedLessonsWithViewType[index]
                        val lessonChapter =
                            chapterLessons.lessons[0]["lesson_chapter"] ?: "Неизвестный раздел"
                        clRootBackground.value = lessonsRoadViewModel.getBackgroundColorForChapter(lessonChapter)
                        cvChapterBottomBackground.value = lessonsRoadViewModel.getColorForChapterCardView(lessonChapter)
                        cvChapterTopBackground.value = lessonsRoadViewModel.getColorForChapterCardView(lessonChapter)
                        tvChapterBottomText.value = lessonChapter
                        tvChapterTopText.value = lessonChapter
                        if (lessonsViewModel.isScreenLarge) {
                            setupLessonsRoadForLargeScreen(
                                ivLineTopLeftWidth = ivLineTopLeftWidth,
                                ivLineTopLeftHeight = ivLineTopLeftHeight,
                                ivLineTopLeftMargins = ivLineTopLeftMargins,
                                ivLineTopRightWidth = ivLineTopRightWidth,
                                ivLineTopRightHeight = ivLineTopRightHeight,
                                ivLineTopRightMargins = ivLineTopRightMargins,
                                ivLineBottomLeftWidth = ivLineBottomLeftWidth,
                                ivLineBottomLeftHeight = ivLineBottomLeftHeight,
                                ivLineBottomLeftMargins = ivLineBottomLeftMargins,
                                ivLineBottomRightWidth = ivLineBottomRightWidth,
                                ivLineBottomRightHeight = ivLineBottomRightHeight,
                                ivLineBottomRightMargins = ivLineBottomRightMargins,
                                lessonsViewModel = lessonsViewModel
                            )
                        } else {
                            setupLessonsRoadForSmallScreen(
                                ivLineTopLeftWidth = ivLineTopLeftWidth,
                                ivLineTopLeftHeight = ivLineTopLeftHeight,
                                ivLineTopLeftMargins = ivLineTopLeftMargins,
                                ivLineTopRightWidth = ivLineTopRightWidth,
                                ivLineTopRightHeight = ivLineTopRightHeight,
                                ivLineTopRightMargins = ivLineTopRightMargins,
                                ivLineBottomLeftWidth = ivLineBottomLeftWidth,
                                ivLineBottomLeftHeight = ivLineBottomLeftHeight,
                                ivLineBottomLeftMargins = ivLineBottomLeftMargins,
                                ivLineBottomRightWidth = ivLineBottomRightWidth,
                                ivLineBottomRightHeight = ivLineBottomRightHeight,
                                ivLineBottomRightMargins = ivLineBottomRightMargins,
                                lessonsViewModel = lessonsViewModel
                            )
                        }
                        // Устанавливаем линию раздела снизу
                        if (index != groupedLessonsWithViewType.lastIndex) {
                            paint.color =
                                lessonsRoadViewModel.getLineColorForChapter(lessons[0]["lesson_chapter"]!!)
                            setBottomLines(
                                position = index,
                                lessonsRoadViewModel = lessonsRoadViewModel,
                                groupedLessonsWithViewType = groupedLessonsWithViewType,
                                ivLineBottomRightRotationX = ivLineBottomRightRotationX,
                                ivLineBottomRight = ivLineBottomRight,
                                ivLineBottomLeft = ivLineBottomLeft,
                                ivLineBottomRightWidth = ivLineBottomRightWidth,
                                ivLineBottomRightHeight = ivLineBottomRightHeight,
                                ivLineBottomLeftWidth = ivLineBottomLeftWidth,
                                ivLineBottomLeftHeight = ivLineBottomLeftHeight,
                                ivLineBottomRightRes = ivLineBottomRightRes,
                                ivLineBottomLeftRes = ivLineBottomLeftRes,
                                paint = paint,
                            )
                        } else {
                            hideBottomLines(
                                ivLineBottomRight = ivLineBottomRight,
                                ivLineBottomLeft = ivLineBottomLeft,
                            )
                            // Устанавливаем для первого раздела отступ снизу,
                            // чтобы не было белого фона по углам меню и раздел отображался корректно
                            clRootMargins.clear()
                            clRootMargins.addAll(listOf(0, 0, 0, 56 + lessonsViewModel.dpToPx(24)))
                            // Делаем, чтобы фон расстягивался на весь экран, не оставляя пустоты
                            clRootMatchParent.value = true
                            // Убираем привязку нижнего названия раздела, чтобы он не расползался
                            cvChapterBottomConstrainTo.value = false

                        }
                        // Устанавливаем линию раздела сверху
                        if (index != 0) {
                            paint.color =
                                lessonsRoadViewModel.getLineColorForChapter(lessons[0]["lesson_chapter"]!!)
                            setTopLines(
                                position = index,
                                groupedLessonsWithViewType = groupedLessonsWithViewType,
                                ivLineTopRightRotationX = ivLineTopRightRotationX,
                                ivLineTopLeftRotationX = ivLineTopLeftRotationX,
                                ivLineTopLeft = ivLineTopLeft,
                                ivLineTopRight = ivLineTopRight,
                                ivLineTopRightWidth = ivLineTopRightWidth,
                                ivLineTopRightHeight = ivLineTopRightHeight,
                                lessonsRoadViewModel = lessonsRoadViewModel,
                                paint = paint,
                                ivLineTopLeftWidth = ivLineTopLeftWidth,
                                ivLineTopLeftHeight = ivLineTopLeftHeight,
                                ivLineTopLeftRes = ivLineTopLeftRes,
                                ivLineTopRightRes = ivLineTopRightRes,
                            )
                        } else {
                            hideTopLines(
                                ivLineTopRight = ivLineTopRight,
                                ivLineTopLeft = ivLineTopLeft
                            )
                        }
                        if (lessonsViewModel.isScreenLarge) {
                            rvRootWidth.value = lessonsViewModel.dpToPx(730)
                        } else {
                            rvRootWidth.value = lessonsViewModel.dpToPx(365)
                        }
                    }
                }


            }

            else -> {}
        }
    }
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val maxHeight = this.maxHeight
        ConstraintLayout(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(verticalScroll)
        ) {
            val boxRef = createRef()
            if (uppButtonLayout.value) {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .zIndex(8.dp.value)
                        .constrainAs(boxRef) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                ) {
                    Button(
                        modifier = modifier
                            .size(44.dp)
                            .padding(20.dp),
                        onClick = {
                            coroutineScope.launch {
                                scrollState.animateScrollTo(0)
                            }
                        }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.upbtn),
                            contentDescription = null,
                        )
                    }
                }
            }
            //NestedScroll
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .verticalScroll(scrollState)
                        .onGloballyPositioned {
                            if (scrollYChanged.value) {
                                if (lessonsRoadViewModel.isFirstOpen) {
                                    coroutineScope.launch {
                                        scrollState.animateScrollTo(lessonsRoadViewModel.scrollY)
                                    }
                                    lessonsRoadViewModel.isFirstOpen = false
                                }
                            }
                        }
                ) {
                    //flRootLessonsRoad
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .onGloballyPositioned {

                            }
                        ,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (loadingLayout.value) {
                            ConstraintLayout(
                                modifier = modifier
                                    .fillMaxSize()
                            ) {
                                var progressBar = createRef()
                                CircularProgressIndicator(
                                    modifier
                                        .wrapContentSize()
                                        .constrainAs(progressBar) {
                                            top.linkTo(parent.top)
                                            bottom.linkTo(parent.bottom)
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                        }
                                )

                            }
                            if (tvErrorLessonsRoadList.value) {
                                Text(
                                    modifier =
                                    modifier
                                        .wrapContentSize(),
                                    textAlign = TextAlign.Center,
                                    text = tvErrorLessonsRoadListText.value
                                )
                            }

                        }


                    }
                }

            }

        }


    }


}

private fun setTopLines(
    position: Int,
    groupedLessonsWithViewType: ArrayList<GroupedLessonsWithViewType>,
    ivLineTopRightRotationX: MutableState<Float>,
    ivLineTopLeftRotationX: MutableState<Float>,
    ivLineTopLeft: MutableState<Boolean>,
    ivLineTopRight: MutableState<Boolean>,
    ivLineTopRightWidth: MutableState<Int>,
    ivLineTopRightHeight: MutableState<Int>,
    lessonsRoadViewModel: LessonsRoadViewModel,
    paint: Paint,
    ivLineTopLeftWidth: MutableState<Int>,
    ivLineTopLeftHeight: MutableState<Int>,
    ivLineTopLeftRes: MutableState<Bitmap?>,
    ivLineTopRightRes: MutableState<Bitmap?>,
    ) {
    circlePath.reset()
    if (groupedLessonsWithViewType[position].viewType == LessonsRoadAdapter.VIEW_TYPE_LEFT) {
        ivLineTopRightRotationX.value = 180f
        ivLineTopLeftRotationX.value = 180f
        ivLineTopLeft.value = true
        topLeftBitmap = lessonsRoadViewModel.createLineBetweenChapters(
            circlePath,
            paint,
            ivLineTopLeftWidth.value,
            ivLineTopLeftHeight.value
        )
        ivLineTopLeftRes.value = topLeftBitmap
        ivLineTopRight.value = false
    } else {
        ivLineTopRightRotationX.value = 0f
        ivLineTopLeftRotationX.value = 0f
        ivLineTopRight.value = true
        topRightBitmap = lessonsRoadViewModel.createLineBetweenChapters(
            circlePath,
            paint,
            ivLineTopRightWidth.value,
            ivLineTopRightHeight.value,
        )
        ivLineTopRightRes.value = topRightBitmap
        ivLineTopLeft.value = false
    }
}

private fun setBottomLines(
    position: Int,
    lessonsRoadViewModel: LessonsRoadViewModel,
    groupedLessonsWithViewType: ArrayList<GroupedLessonsWithViewType>,
    ivLineBottomRightRotationX: MutableState<Float>,
    ivLineBottomRight: MutableState<Boolean>,
    ivLineBottomLeft: MutableState<Boolean>,
    ivLineBottomRightWidth: MutableState<Int>,
    ivLineBottomRightHeight: MutableState<Int>,
    ivLineBottomLeftWidth: MutableState<Int>,
    ivLineBottomLeftHeight: MutableState<Int>,
    ivLineBottomRightRes: MutableState<Bitmap?>,
    ivLineBottomLeftRes: MutableState<Bitmap?>,
    paint: Paint,
) {
    circlePath.reset()
    if (groupedLessonsWithViewType[position + 1].viewType == LessonsRoadAdapter.VIEW_TYPE_LEFT) {
        ivLineBottomRightRotationX.value = 180f
        ivLineBottomRight.value = true
        ivLineBottomRightRes.value = lessonsRoadViewModel.createLineBetweenChapters(
            circlePath,
            paint,
            ivLineBottomRightWidth.value,
            ivLineBottomRightHeight.value
        )
        ivLineBottomLeft.value = false
    } else {
        ivLineBottomRightRotationX.value = 0f
        ivLineBottomLeft.value = true
        ivLineBottomLeftRes.value = lessonsRoadViewModel.createLineBetweenChapters(
            circlePath,
            paint,
            ivLineBottomLeftWidth.value,
            ivLineBottomLeftHeight.value
        )
        ivLineBottomRight.value = false
    }
}

// Скрываем линии
private fun hideBottomLines(
    ivLineBottomRight: MutableState<Boolean>,
    ivLineBottomLeft: MutableState<Boolean>,
) {
    ivLineBottomRight.value = false
    ivLineBottomLeft.value = false
}

// Скрываем линии
private fun hideTopLines(
    ivLineTopRight: MutableState<Boolean>,
    ivLineTopLeft: MutableState<Boolean>,
) {
    ivLineTopRight.value = false
    ivLineTopLeft.value = false
}

private fun setupLessonsRoadForLargeScreen(
    ivLineTopLeftWidth: MutableState<Int>,
    ivLineTopLeftHeight: MutableState<Int>,
    ivLineTopLeftMargins: SnapshotStateList<Int>,
    ivLineTopRightWidth: MutableState<Int>,
    ivLineTopRightHeight: MutableState<Int>,
    ivLineTopRightMargins: SnapshotStateList<Int>,
    ivLineBottomLeftWidth: MutableState<Int>,
    ivLineBottomLeftHeight: MutableState<Int>,
    ivLineBottomLeftMargins: SnapshotStateList<Int>,
    ivLineBottomRightWidth: MutableState<Int>,
    ivLineBottomRightHeight: MutableState<Int>,
    ivLineBottomRightMargins: SnapshotStateList<Int>,
    lessonsViewModel: LessonsViewModel
) {
    ivLineTopLeftWidth.value = lessonsViewModel.dpToPx(274)
    ivLineTopLeftHeight.value = lessonsViewModel.dpToPx(113)
    ivLineTopLeftMargins.clear()
    ivLineTopLeftMargins.addAll(
        listOf(
            lessonsViewModel.dpToPx(95.6f),
            -lessonsViewModel.dpToPx(6),
            0,
            0
        )
    )
    ivLineTopRightWidth.value = lessonsViewModel.dpToPx(250)
    ivLineTopRightHeight.value = lessonsViewModel.dpToPx(115)
    ivLineTopRightMargins.clear()
    ivLineTopRightMargins.addAll(
        listOf(
            0,
            lessonsViewModel.dpToPx(10),
            lessonsViewModel.dpToPx(102),
            0
        )
    )
    ivLineBottomLeftWidth.value = lessonsViewModel.dpToPx(288)
    ivLineBottomLeftHeight.value = lessonsViewModel.dpToPx(115)
    ivLineBottomLeftMargins.clear()
    ivLineBottomLeftMargins.addAll(listOf(0, 0, 0, -lessonsViewModel.dpToPx(5.5f)))
    ivLineBottomRightWidth.value = lessonsViewModel.dpToPx(276.5f)
    ivLineBottomRightHeight.value = lessonsViewModel.dpToPx(115)
    ivLineBottomRightMargins.clear()
    ivLineBottomRightMargins.addAll(listOf(0, 0, 0, -lessonsViewModel.dpToPx(2)))
}

private fun setupLessonsRoadForSmallScreen(
    ivLineTopLeftWidth: MutableState<Int>,
    ivLineTopLeftHeight: MutableState<Int>,
    ivLineTopLeftMargins: SnapshotStateList<Int>,
    ivLineTopRightWidth: MutableState<Int>,
    ivLineTopRightHeight: MutableState<Int>,
    ivLineTopRightMargins: SnapshotStateList<Int>,
    ivLineBottomLeftWidth: MutableState<Int>,
    ivLineBottomLeftHeight: MutableState<Int>,
    ivLineBottomLeftMargins: SnapshotStateList<Int>,
    ivLineBottomRightWidth: MutableState<Int>,
    ivLineBottomRightHeight: MutableState<Int>,
    ivLineBottomRightMargins: SnapshotStateList<Int>,
    lessonsViewModel: LessonsViewModel
) {
    ivLineTopLeftWidth.value = lessonsViewModel.dpToPx(100)
    ivLineTopLeftMargins.clear()
    ivLineTopLeftMargins.addAll(
        listOf(
            lessonsViewModel.dpToPx(84),
            -lessonsViewModel.dpToPx(19),
            0,
            0
        )
    )
    ivLineTopRightWidth.value = lessonsViewModel.dpToPx(95)
    ivLineTopRightHeight.value = lessonsViewModel.dpToPx(105)
    ivLineTopRightMargins.clear()
    ivLineTopRightMargins.addAll(listOf(0, 0, lessonsViewModel.dpToPx(90), 0))
    ivLineBottomLeftWidth.value = lessonsViewModel.dpToPx(90)
    ivLineBottomLeftHeight.value = lessonsViewModel.dpToPx(105)
    ivLineBottomLeftMargins.clear()
    ivLineBottomLeftMargins.addAll(listOf(0, 0, 0, -lessonsViewModel.dpToPx(2)))
    ivLineBottomRightWidth.value = lessonsViewModel.dpToPx(100)
    ivLineBottomRightHeight.value = lessonsViewModel.dpToPx(112)
    ivLineBottomRightMargins.clear()
    ivLineBottomRightMargins.addAll(listOf(0, 0, 0, -lessonsViewModel.dpToPx(2)))
}

@Preview
@Composable
fun LessonsRoadFragmentPreview() {
    LessonsRoadScreen(
        modifier = Modifier
    )
}