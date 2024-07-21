package com.example.practiceandroid.modules.lessonsRoad.views

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.FrameLayout
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.practiceandroid.R
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadViewModel
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsViewModel
import com.skydoves.landscapist.rememberDrawablePainter

@Composable
fun LessonsRoadAdapterScreen(
    modifier: Modifier = Modifier,
    lessons: ArrayList<Map<String, String>>,
    isScreenLarge: Boolean,
    lessonsRoadViewModel: LessonsRoadViewModel,
    lessonsViewModel: LessonsViewModel,
    previousItemViewType: Int,
    isScrollAdapter: Boolean = false,
    // Слушатель нажатия по кружку урока
    onLessonClick: (lesson: Map<String, String>) -> Unit,
) {
    // Константы для определения типа View
    val VIEW_TYPE_RIGHT = 0
    val VIEW_TYPE_LEFT = 1

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = lessonsRoadViewModel.getLineColorForChapter(lessons[0]["lesson_chapter"]!!)
    }
//поменять на column
    Column(
        modifier = modifier
            .width(365.dp)
            .wrapContentHeight()
            .padding(top = 24.dp),

        ) {
        for (index in 0..lessons.lastIndex) {
            Log.d("KIK", "adapter created")
            val viewType = if (index % 2 == 0) VIEW_TYPE_RIGHT else VIEW_TYPE_LEFT
            Log.d("KIK", "viewType = $viewType")
            when (viewType) {
                VIEW_TYPE_RIGHT -> RightLessonItem(
                    position = index,
                    lessons = lessons,
                    onLessonClick = onLessonClick,
                    lesson = lessons[index],
                    modifier = modifier,
                    previousItemViewType = previousItemViewType,
                    lessonsRoadViewModel = lessonsRoadViewModel,
                    isScreenLarge = isScreenLarge,
                    lessonsViewModel = lessonsViewModel,
                    paint = paint,
                    isScrollAdapter = isScrollAdapter
                )

                VIEW_TYPE_LEFT -> LeftLessonItem(
                    position = index,
                    lessons = lessons,
                    onLessonClick = onLessonClick,
                    lesson = lessons[index],
                    modifier = modifier,
                    previousItemViewType = previousItemViewType,
                    lessonsRoadViewModel = lessonsRoadViewModel,
                    isScreenLarge = isScreenLarge,
                    lessonsViewModel = lessonsViewModel,
                    paint = paint,
                    isScrollAdapter = isScrollAdapter
                )
                //else -> ErrorLessonItem()
            }
        }
    }
}

@Composable
fun RightLessonItem(
    paint: Paint,
    position: Int,
    previousItemViewType: Int,
    modifier: Modifier,
    lesson: Map<String, String>,
    lessons: ArrayList<Map<String, String>>,
    lessonsRoadViewModel: LessonsRoadViewModel,
    lessonsViewModel: LessonsViewModel,
    isScreenLarge: Boolean,
    onLessonClick: (lesson: Map<String, String>) -> Unit,
    isScrollAdapter: Boolean,
) {//возможно переделывать надо будет
    val circlePath = Path()
    var lineToRight: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    var lineToLeft: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    //Отступы для flRootLayout
    var marginsForFL = remember { mutableStateListOf(25, -50, 0, 0) }
    var ivLineMargins = remember { mutableStateListOf(-10, 0, 0, 4) }
    var ivLineRight = remember { mutableStateOf(false) }
    var ivLineLeft = remember { mutableStateOf(false) }
    var ivLineRes: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    var ivLineWidthLeft = remember { mutableStateOf(70) }
    var ivLineHeightLeft = remember { mutableStateOf(60) }
    var ivLineWidthRight = remember { mutableStateOf(120) }
    var ivLineHeightRight = remember { mutableStateOf(177) }
    var ivLineRotationY = remember { mutableStateOf(0f) }
    var llHorizontalContainer: MutableState<Dp?> = remember { mutableStateOf(240.dp) }
    var marginTop = remember { mutableStateOf(0) }
    var measuredHeight = remember { mutableStateOf(0) }
    val colorForCV = remember { mutableStateOf(Color(0xFF4CAF50)) }
    //try {
    if (previousItemViewType == LessonsRoadAdapter.VIEW_TYPE_LEFT) {
        Log.d("KIK", "RightLesson viewTypeLeft and setupRightSide")
        SetupLinesWhenFirstElementInRightSide(
            position = position,
            lessons = lessons,
            marginsForFL = marginsForFL,
            ivLineRight = ivLineRight,
            ivLineWidth = ivLineWidthRight,
            ivLineHeight = ivLineHeightRight,
            lessonsViewModel = lessonsViewModel,
            lessonsRoadViewModel = lessonsRoadViewModel,
            ivLineRotationY = ivLineRotationY,
            isScreenLarge = isScreenLarge,
            ivLineMargins = ivLineMargins,
            lineToRight = lineToRight,
            lineToLeft = lineToLeft,
            circlePath = circlePath,
            paint = paint,
            llHorizontalContainer = llHorizontalContainer,
            ivLineRes = ivLineRes,
        )
    } else {
        Log.d("KIK", "RightLesson viewTypeRight and setupLeftSide")
        SetupLinesWhenFirstElementInLeftSide(
            position = position,
            lessons = lessons,
            marginsForFL = marginsForFL,
            ivLineLeft = ivLineLeft,
            ivLineWidth = ivLineWidthLeft,
            ivLineRes = ivLineRes,
            ivLineMargins = ivLineMargins,
            ivLineRotationY = ivLineRotationY,
            lineToRight = lineToRight,
            lineToLeft = lineToLeft,
            lessonsViewModel = lessonsViewModel,
            isScreenLarge = isScreenLarge,
            llHorizontalContainer = llHorizontalContainer,
            circlePath = circlePath,
            lessonsRoadViewModel = lessonsRoadViewModel,
            paint = paint,
            ivLineHeight = ivLineHeightLeft,
        )
    }
    if (isScrollAdapter && lessonsRoadViewModel.firstUnfulfilledLesson!!["lesson_number"]!!.toInt() < lesson["lesson_number"]!!.toInt()) {
        lessonsRoadViewModel.scrollRecyclerHeightSumAfterScrollElement += marginsForFL[1]
        lessonsRoadViewModel.scrollRecyclerHeightSumAfterScrollElement += measuredHeight.value
    }
    Box(
        modifier = modifier
            .padding(
                start = marginsForFL[0].dp,
                end = marginsForFL[2].dp,
                bottom = marginsForFL[3].dp
            )
            .fillMaxWidth()
            .offset(y = (marginsForFL[1]).dp)
            .wrapContentHeight()
            //Получение высоты после flRootLayout после постройки UI
            .onGloballyPositioned { layoutCoordinates ->
                measuredHeight.value = layoutCoordinates.size.height
                Log.d("LOL", "measuredHeight changed = ${measuredHeight.value}")
            },
        contentAlignment = Alignment.CenterEnd
    ) {
        //llHorizontalContainer
        Row(
            modifier = modifier
                .wrapContentWidth()
                .height(llHorizontalContainer.value!!)
        ) {
            if (ivLineRight.value == true && ivLineRes.value != null) {
                Log.d("kik", "lineRightEl is created")
                //id = ivForLine
                Image(
                    modifier = modifier
                        .width(ivLineWidthRight.value.dp)
                        .height(ivLineHeightRight.value.dp)
                        //.padding(bottom = 4.dp, start = (-10).dp)
                        .offset(y = (-4).dp, x = (-10).dp)
                        .graphicsLayer {
                            rotationY = 0f
                        },
                    bitmap = ivLineRes.value!!.asImageBitmap(),
                    contentDescription = null,
                )
            } else Log.d("kik", "lineRight is not created")
            //llLessonContainer
            //Отрисовка кружка с уроком
            Column(
                modifier = modifier
                    .wrapContentHeight()
                    .clickable { onLessonClick(lesson) }
                    .width(130.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Box(
                    modifier = modifier
                        .size(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .align(Alignment.Center),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        border = BorderStroke(width = 4.dp, color = Color.White),
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(0.dp),
                    ) {
                        val context = LocalContext.current
                        val image = remember { mutableStateOf<Drawable?>(null) }
                        if (image.value == null) {
                            Glide.with(LocalContext.current)
                                .load(""/*ConfigData.BASE_URL + lesson["lesson_img_adr"]*/)
                                .apply(RequestOptions().centerCrop())
                                .into(
                                    object : CustomTarget<Drawable>() {
                                        override fun onResourceReady(
                                            resource: Drawable,
                                            transition: Transition<in Drawable>?
                                        ) {
                                            Log.d("LOL", "onResourceReady")
                                            image.value = resource
                                        }

                                        override fun onLoadFailed(errorDrawable: Drawable?) {
                                            super.onLoadFailed(errorDrawable)
                                            Log.d("LOL", "onLoadFailed")
                                            image.value = ContextCompat.getDrawable(
                                                context,
                                                R.drawable.no_lesson_image
                                            )
                                        }

                                        override fun onLoadCleared(placeholder: Drawable?) {
                                            // Handle when the image load is cleared
                                        }
                                    }
                                )
                        }
                        Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center

                        ) {
                            if (image.value == null) {
                                CircularProgressIndicator(
                                    modifier = modifier
                                        .fillMaxSize()
                                        .padding(40.dp)
                                )
                            } else {
                                Image(
                                    modifier = modifier
                                        .graphicsLayer(
                                            scaleX = 1.21f,
                                            scaleY = 1.21f
                                        )
                                        .fillMaxSize(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    painter = rememberDrawablePainter(image.value)
                                )
                            }
                        }
                    }
                }
                Log.d("KEK", "colorForCV = ${colorForCV.value}")
                lesson["status"]?.let {
                    colorForCV.value = Color(lessonsRoadViewModel.getLessonsStatusColorById(it))
                    Log.d("KEK", "color = ${lessonsRoadViewModel.getLessonsStatusColorById(it)}")
                }
                Card(
                    modifier = modifier
                        .height(22.dp)
                        .offset(y = (-10).dp)
                        .wrapContentWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = colorForCV.value // Задание цвета фона
                    ),
                    elevation = CardDefaults.cardElevation(0.dp), // Убрана тень
                    shape = MaterialTheme.shapes.medium, // Задание скругленных углов
                ) {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(
                                start = 10.dp,
                                top = 4.dp,
                                end = 10.dp,
                                bottom = 4.dp
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = lesson["status"]?.let {
                                lessonsRoadViewModel.setLessonStatusNameById(it)
                            } ?: "Default status",
                            modifier = modifier
                                .wrapContentWidth()
                                .wrapContentHeight(),
                            fontSize = 10.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Text(
                    text = "${lesson["lesson_number"]}." + "\u00A0" + "${lesson["lesson_short_name"]}",
                    textAlign = TextAlign.Center,
                    modifier = modifier
                        .wrapContentHeight()
                        .offset(y = (-4.dp))
                        .fillMaxWidth(),
                    maxLines = 4,
                    fontSize = 13.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = colorResource(id = R.color.lesson_text_color),
                    fontFamily = FontFamily(Font(R.font.montserrat_medium_new)),
                )
            }
        }
    }
    circlePath.reset()

}

@Composable
fun LeftLessonItem(
    paint: Paint,
    position: Int,
    previousItemViewType: Int,
    modifier: Modifier,
    lesson: Map<String, String>,
    lessons: ArrayList<Map<String, String>>,
    lessonsRoadViewModel: LessonsRoadViewModel,
    lessonsViewModel: LessonsViewModel,
    isScreenLarge: Boolean,
    onLessonClick: (lesson: Map<String, String>) -> Unit,
    isScrollAdapter: Boolean,
) {//возможно переделывать надо будет
    val circlePath = Path()
    var lineToRight: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    var lineToLeft: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    //Отступы для flRootLayout
    var marginsForFL = remember { mutableStateListOf(0, -130, 25, 0) }
    var ivLineMargins = remember { mutableStateListOf(-10, 0, 0, 4) }
    var ivLineRight = remember { mutableStateOf(false) }
    var ivLineLeft = remember { mutableStateOf(false) }
    var ivLineRes: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    var ivLineWidthLeft = remember { mutableStateOf(70) }
    var ivLineHeightLeft = remember { mutableStateOf(60) }
    var ivLineWidthRight = remember { mutableStateOf(120) }
    var ivLineHeightRight = remember { mutableStateOf(177) }
    var ivLineRotationY = remember { mutableStateOf(0f) }
    var llHorizontalContainer: MutableState<Dp?> = remember { mutableStateOf(null) }
    var marginTop = remember { mutableStateOf(0) }
    var measuredHeight = remember { mutableStateOf(0) }
    val colorForCV = remember { mutableStateOf(Color(0xFF4CAF50)) }
    //try {
    Box(
        modifier = modifier
            .padding(
                start = marginsForFL[0].dp,
                end = marginsForFL[2].dp,
                bottom = marginsForFL[3].dp
            )
            .fillMaxWidth()
            .offset(y = marginsForFL[1].dp)
            .wrapContentHeight()
            //Получение высоты после flRootLayout после постройки UI
            .onGloballyPositioned { layoutCoordinates ->
                measuredHeight.value = layoutCoordinates.size.height
            }
    ) {
        Row(
            modifier = if (llHorizontalContainer.value == null) {
                modifier
                    .wrapContentHeight()
                    .wrapContentWidth(Alignment.End)
            } else {
                modifier
                    .height(llHorizontalContainer.value!!)
                    .wrapContentWidth(Alignment.End)
            }
        ) {
            Column(
                modifier = modifier
                    //.fillMaxHeight()
                    .wrapContentHeight()
                    .zIndex(1f)
                    .clickable { onLessonClick(lesson) }
                    .width(130.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Box(
                    modifier = Modifier
                        .size(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .align(Alignment.Center),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        border = BorderStroke(width = 4.dp, color = Color.White),
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        var context = LocalContext.current
                        var image = remember { mutableStateOf<Drawable?>(null) }
                        if (image.value == null) {
                            Glide.with(LocalContext.current)
                                .load(""/*ConfigData.BASE_URL + lesson["lesson_img_adr"]*/)
                                .apply(RequestOptions().centerCrop())
                                .into(
                                    object : CustomTarget<Drawable>() {
                                        override fun onResourceReady(
                                            resource: Drawable,
                                            transition: Transition<in Drawable>?
                                        ) {
                                            image.value = resource
                                        }

                                        override fun onLoadFailed(errorDrawable: Drawable?) {
                                            super.onLoadFailed(errorDrawable)
                                            image.value = ContextCompat.getDrawable(
                                                context,
                                                R.drawable.no_lesson_image
                                            )
                                        }

                                        override fun onLoadCleared(placeholder: Drawable?) {
                                            // Handle when the image load is cleared
                                        }
                                    }
                                )
                        }
                        Box(
                            modifier = modifier.size(120.dp),
                            contentAlignment = Alignment.Center

                        ) {
                            if (image.value == null) {
                                CircularProgressIndicator(
                                    modifier = modifier
                                        .fillMaxSize()
                                        .padding(40.dp)
                                )
                            } else {
                                Image(
                                    modifier = modifier
                                        .graphicsLayer(
                                            scaleX = 1.21f,
                                            scaleY = 1.21f
                                        )
                                        .fillMaxSize(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    painter = rememberDrawablePainter(image.value)

                                )
                            }
                        }
                    }
                }
                lesson["status"]?.let {
                    colorForCV.value = Color(lessonsRoadViewModel.getLessonsStatusColorById(it))
                }

                Card(
                    modifier = Modifier
                        .height(22.dp)
                        .offset(y = (-10.dp))
                        .wrapContentWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = colorForCV.value // Задание цвета фона
                    ),
                    elevation = CardDefaults.cardElevation(0.dp), // Убрана тень
                    shape = MaterialTheme.shapes.medium, // Задание скругленных углов
                ) {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(
                                start = 10.dp,
                                top = 4.dp,
                                end = 10.dp,
                                bottom = 4.dp
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = lesson["status"]?.let {
                                lessonsRoadViewModel.setLessonStatusNameById(it)
                            } ?: "Default status",
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight(),
                            fontSize = 10.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Text(
                    text = "${lesson["lesson_number"]}." + "\u00A0" + "${lesson["lesson_short_name"]}",
                    textAlign = TextAlign.Center,
                    modifier = modifier
                        .wrapContentHeight()
                        .offset(y = (-4).dp)
                        .fillMaxWidth(),
                    maxLines = 4,
                    fontSize = 13.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = colorResource(id = R.color.lesson_text_color),
                    fontFamily = FontFamily(Font(R.font.montserrat_medium_new)),
                )
            }

            if (previousItemViewType == LessonsRoadAdapter.VIEW_TYPE_LEFT) {
                Log.d("KIK", "LeftLesson viewTypeLeft and setupRightSide")
                SetupLinesWhenFirstElementInRightSide(
                    position = position,
                    lessons = lessons,
                    marginsForFL = marginsForFL,
                    ivLineRight = ivLineRight,
                    ivLineWidth = ivLineWidthRight,
                    ivLineHeight = ivLineHeightRight,
                    lessonsViewModel = lessonsViewModel,
                    lessonsRoadViewModel = lessonsRoadViewModel,
                    ivLineRotationY = ivLineRotationY,
                    isScreenLarge = isScreenLarge,
                    ivLineMargins = ivLineMargins,
                    lineToRight = lineToRight,
                    lineToLeft = lineToLeft,
                    circlePath = circlePath,
                    paint = paint,
                    llHorizontalContainer = llHorizontalContainer,
                    ivLineRes = ivLineRes,
                )
            } else if (previousItemViewType == LessonsRoadAdapter.VIEW_TYPE_RIGHT) {
                Log.d("KIK", "LeftLesson viewTypeRight and setupLeftSide")
                SetupLinesWhenFirstElementInLeftSide(
                    position = position,
                    lessons = lessons,
                    marginsForFL = marginsForFL,
                    ivLineLeft = ivLineLeft,
                    ivLineWidth = ivLineWidthLeft,
                    ivLineRes = ivLineRes,
                    ivLineMargins = ivLineMargins,
                    ivLineRotationY = ivLineRotationY,
                    lineToRight = lineToRight,
                    lineToLeft = lineToLeft,
                    lessonsViewModel = lessonsViewModel,
                    isScreenLarge = isScreenLarge,
                    llHorizontalContainer = llHorizontalContainer,
                    circlePath = circlePath,
                    lessonsRoadViewModel = lessonsRoadViewModel,
                    paint = paint,
                    ivLineHeight = ivLineHeightLeft,
                )
            }
            if (isScrollAdapter && lessonsRoadViewModel.firstUnfulfilledLesson!!["lesson_number"]!!.toInt() < lesson["lesson_number"]!!.toInt()) {
                lessonsRoadViewModel.scrollRecyclerHeightSumAfterScrollElement += marginsForFL[1]
                lessonsRoadViewModel.scrollRecyclerHeightSumAfterScrollElement += measuredHeight.value
            }


            if (ivLineRight.value && ivLineRes.value != null) {
                //id = ivForLine
                Image(
                    modifier = modifier
                        .width(ivLineWidthRight.value.dp)
                        .height(ivLineHeightRight.value.dp)
                        .offset(x = (-10).dp, y = 70.dp)
                        .align(Alignment.Bottom)
                        .zIndex(0f)
                        //.padding(bottom = 4.dp, start = (-10).dp)
                        .graphicsLayer {
                            rotationY = 0f
                        },
                    bitmap = ivLineRes.value!!.asImageBitmap(),
                    contentDescription = null,
                )
            }
        }
    }
    /*} catch (e: Exception) {
        Log.d("LessonRoadAdapter", e.message.toString())
    }

     */
    circlePath.reset()

}

@Composable
fun SetupLinesWhenFirstElementInLeftSide(
    position: Int,
    lessons: ArrayList<Map<String, String>>,
    marginsForFL: SnapshotStateList<Int>,
    ivLineLeft: MutableState<Boolean>,
    ivLineWidth: MutableState<Int>,
    ivLineRes: MutableState<Bitmap?>,
    ivLineMargins: SnapshotStateList<Int>,
    ivLineRotationY: MutableState<Float>,
    lineToRight: MutableState<Bitmap?>,
    lineToLeft: MutableState<Bitmap?>,
    lessonsViewModel: LessonsViewModel,
    isScreenLarge: Boolean,
    llHorizontalContainer: MutableState<Dp?>,
    circlePath: Path,
    lessonsRoadViewModel: LessonsRoadViewModel,
    paint: Paint,
    ivLineHeight: MutableState<Int>,
) {
    if (lessons.size == 1) {
        marginsForFL.clear()
        marginsForFL.addAll(listOf(0, 0, 0, 0))
        ivLineLeft.value = false
    } else {
        // Если это первый кружок или если это кружок справа, убираем отрицательный верхний отступ, иначе оставляем
        if (position == 0) {
            marginsForFL.clear()
            marginsForFL.addAll(listOf(0, 0, 0, 0))
        } else if (position % 2 != 0) {
            marginsForFL.clear()
            marginsForFL.addAll((listOf(0, -lessonsViewModel.dpToPx(130), 0, 0)))
        } else {
            marginsForFL.clear()
            marginsForFL.addAll(listOf(0, -lessonsViewModel.dpToPx(50), 0, 0))
        }
        if (isScreenLarge) {
            ivLineWidth.value = lessonsViewModel.dpToPx(435)
        } else {
            ivLineWidth.value = lessonsViewModel.dpToPx(120)
        }
        //Сомнительный момент, мб нулл будет пихать
        if (position != lessons.lastIndex) {
            ivLineLeft.value = true
            if (position == 0) {
                marginsForFL.clear()
                marginsForFL.addAll(listOf(0, 0, 0, 0))
            } else if (position % 2 == 0) {
                marginsForFL.clear()
                marginsForFL.addAll(listOf(0, -lessonsViewModel.dpToPx(50), 0, 0))
            } else {
                marginsForFL.clear()
                marginsForFL.addAll(listOf(0, -lessonsViewModel.dpToPx(130), 0, 0))
            }
            if (position % 2 == 0 && position != 0) {
                marginsForFL.clear()
                marginsForFL.addAll(listOf(0, -lessonsViewModel.dpToPx(50), 0, 0))
            }
            if (position % 2 == 0) {
                ivLineRotationY.value = 180f
                if (isScreenLarge) {
                    ivLineWidth.value = 435
                    ivLineMargins.clear()
                    ivLineMargins.addAll(listOf(0, 0, 0, 0))
                } else {
                    ivLineWidth.value = 70
                    ivLineMargins.clear()
                    ivLineMargins.addAll(listOf(0, 0, -lessonsViewModel.dpToPx(10)))
                }
                if (lineToLeft.value == null) {
                    lineToLeft.value = lessonsRoadViewModel.createLineBitmapRightToLeft(
                        path = circlePath,
                        paint = paint,
                        width = ivLineWidth.value,
                        height = ivLineHeight.value
                        //height = ivLineHeight.value - lessonsViewModel.dpToPx(50)
                    )
                }
                ivLineRes.value = lineToLeft.value
            } else {
                ivLineRotationY.value = 0f
                if (isScreenLarge) ivLineWidth.value = lessonsViewModel.dpToPx(470)
                else {
                    ivLineWidth.value = lessonsViewModel.dpToPx(120)
                }
                if (lineToRight.value == null) {
                    lineToRight.value = lessonsRoadViewModel.createLineBitmapLeftToRight(
                        path = circlePath,
                        paint = paint,
                        width = ivLineWidth.value,
                        height = ivLineHeight.value
                        //height = ivLineHeight.value - lessonsViewModel.dpToPx(50)
                    )
                }
                ivLineRes.value = lineToRight.value
            }
        }
    }
    if (position == lessons.lastIndex) {
        llHorizontalContainer.value = FrameLayout.LayoutParams.WRAP_CONTENT.dp
        ivLineLeft.value = false
    } else {
        ivLineLeft.value = true
        llHorizontalContainer.value = lessonsViewModel.dpToPx(240).dp
    }
}

// Установка линий, когда первый элемент находиться на правой стороне
@Composable
fun SetupLinesWhenFirstElementInRightSide(
    position: Int,
    lessons: ArrayList<Map<String, String>>,
    marginsForFL: SnapshotStateList<Int>,
    ivLineRight: MutableState<Boolean>,
    ivLineWidth: MutableState<Int>,
    ivLineHeight: MutableState<Int>,
    lessonsViewModel: LessonsViewModel,
    lessonsRoadViewModel: LessonsRoadViewModel,
    ivLineRotationY: MutableState<Float>,
    isScreenLarge: Boolean,
    ivLineMargins: SnapshotStateList<Int>,
    lineToRight: MutableState<Bitmap?>,
    lineToLeft: MutableState<Bitmap?>,
    circlePath: Path,
    paint: Paint,
    llHorizontalContainer: MutableState<Dp?>,
    ivLineRes: MutableState<Bitmap?>,
) {
    if (lessons.size == 1) {
        marginsForFL.clear()
        marginsForFL.addAll(listOf(0, 0, 0, 0))
        ivLineRight.value = false
    } else {
        if (position != lessons.lastIndex) {
            ivLineRight.value = true
            if (position == 0) {
                marginsForFL.clear()
                marginsForFL.addAll(listOf(0, 0, 0, 0))
            } else if (position == 1) {
                marginsForFL.clear()
                marginsForFL.addAll(listOf(0,-130,0,0))
            } else if (position % 2 != 0) {
                marginsForFL.clear()
                marginsForFL.addAll(listOf(0, -200, 0, 0))
                //marginsForFL.addAll(listOf(0, -lessonsViewModel.dpToPx(130), 0, 0))
            } else {
                marginsForFL.clear()
                marginsForFL.addAll(listOf(0, -200, 0, 0))
            }
        } else {
            ivLineRight.value = false
        }
        if (position % 2 == 0) {
            ivLineRotationY.value = 0f
            if (isScreenLarge) {
                ivLineWidth.value = 470
            } else {
                ivLineWidth.value = 120
            }
            //Здесь фиксить надо
            if (lineToRight.value == null) {
                lineToRight.value = lessonsRoadViewModel.createLineBitmapLeftToRight(
                    path = circlePath,
                    paint = paint,
                    width = ivLineWidth.value,
                    height = ivLineHeight.value - lessonsViewModel.dpToPx(50)

                )
            }
            ivLineRes.value = lineToRight.value
        } else {
            ivLineRotationY.value = 180f
            if (isScreenLarge) {
                ivLineWidth.value = lessonsViewModel.dpToPx(435)
                ivLineMargins.clear()
                ivLineMargins.addAll(listOf(0, 0, 0, 0))
                //Я не понял зачем в XML коде 2 раза ставились margin
                ivLineMargins.clear()
                ivLineMargins.addAll(listOf(0, 0, 0, 0))
            } else {
                ivLineWidth.value = lessonsViewModel.dpToPx(70)
                ivLineMargins.clear()
                ivLineMargins.addAll(listOf(0, 0, -lessonsViewModel.dpToPx(10), 0))
            }
            if (lineToLeft.value == null) {
                lineToLeft.value = lessonsRoadViewModel.createLineBitmapRightToLeft(
                    path = circlePath,
                    paint = paint,
                    width = ivLineWidth.value,
                    height = ivLineHeight.value
                )
            }
            ivLineRes.value = lineToLeft.value
        }
    }
    if (position == lessons.lastIndex) {
        llHorizontalContainer.value = FrameLayout.LayoutParams.WRAP_CONTENT.dp
        ivLineRight.value = false
    } else {
        ivLineRight.value = true
        //llHorizontalContainer.value = lessonsViewModel.dpToPx(240).dp
    }
}

@Preview
@Composable
fun LessonsRoadAdapterScreenPreview() {
    var lessonsViewModel = LessonsViewModel(LocalContext.current.applicationContext as Application)
    var lessonsRoadViewModel =
        LessonsRoadViewModel(LocalContext.current.applicationContext as Application)
    val roadlist = arrayListOf<Map<String, String>>()
    if (lessonsRoadViewModel.lessonsRoadList != null) {
        // Установка списка дорожки уроков
        for (road in lessonsRoadViewModel.lessonsRoadList!!) {
            roadlist.add(road)
        }
        lessonsRoadViewModel.groupedLessons =
            lessonsRoadViewModel.getLessonsByChapter(roadlist)

        /*LessonsRoadAdapterScreen(
            lessons = lessonsRoadViewModel.groupedLessons[lessonsRoadViewModel.groupedLessons.lastIndex],
            isScreenLarge = false,
            lessonsRoadViewModel = lessonsRoadViewModel,
            lessonsViewModel = lessonsViewModel,
            previousItemViewType = 1,
            isScrollAdapter = false,
            // Слушатель нажатия по кружку урока
            onLessonClick = {},
        )*/


        lessonsRoadViewModel.groupedLessons.forEachIndexed { index, lessons ->
            Log.d("LOL", "lessons = $index($lessons)")
            LessonsRoadAdapterScreen(
                lessons = lessons,
                isScreenLarge = false,
                lessonsRoadViewModel = lessonsRoadViewModel,
                lessonsViewModel = lessonsViewModel,
                previousItemViewType = 1,
                isScrollAdapter = false,
                // Слушатель нажатия по кружку урока
                onLessonClick = {},
            )
        }
    }
}




