package com.example.practiceandroid.modules.lessonsRoad.views

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.Path
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.fontResource
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.practiceandroid.R
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadViewModel
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsViewModel

@Composable
fun LessonsRoadAdapterScreen(
    modifier: Modifier,
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
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 24.dp),

    ) {
        itemsIndexed(lessons) { index, lesson ->
            val viewType = if (index % 2 == 0) VIEW_TYPE_RIGHT else VIEW_TYPE_LEFT
            when (viewType) {
                VIEW_TYPE_RIGHT -> RightLessonItem(
                    position = index,
                    lessons = lessons,
                    onLessonClick = onLessonClick,
                    lesson = lesson,
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
                    lesson = lesson,
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
    var marginsForFL = remember { mutableStateListOf(25,-130,0,0) }
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
    //try {
        Box (
            modifier = modifier
                //.padding(start = marginsForFL[0].dp, top = (marginsForFL[1]).dp, end = marginsForFL[2].dp, bottom = marginsForFL[3].dp)
                .padding(start = marginsForFL[0].dp, end = marginsForFL[2].dp, bottom = marginsForFL[3].dp)
                .fillMaxWidth()
                .offset(y = (marginsForFL[1]).dp)
                .wrapContentHeight()
                //Получение высоты после flRootLayout после постройки UI
                .onGloballyPositioned { layoutCoordinates ->
                    measuredHeight.value = layoutCoordinates.size.height
                }
        ) {
            Row (
                modifier = modifier
                    .fillMaxWidth()
                    .height(llHorizontalContainer.value!!)
            ){
                if (ivLineLeft.value != null && ivLineRes.value != null) {
                    Image(
                        modifier = modifier
                            .width(ivLineWidthLeft.value.dp)
                            .height(ivLineHeightLeft.value.dp)
                            .padding(end = (-10).dp)
                            .graphicsLayer {
                                rotationY = 0f
                            },
                        bitmap = ivLineRes.value!!.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Column (
                    modifier = modifier
                        .fillMaxHeight()
                        .clickable { onLessonClick(lesson) }
                        .width(130.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    Box (
                        modifier = Modifier
                            .size(120.dp)
                            .padding(horizontal = 10.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Card (
                            modifier = Modifier
                                .matchParentSize()
                                .align(Alignment.Center),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            shape = CircleShape,
                            elevation = CardDefaults.cardElevation(0.dp)
                        ){
                            AndroidView(
                                factory = { context ->
                                    ImageView(context).apply {
                                        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.lesson_circle_background))
                                        scaleType = ImageView.ScaleType.CENTER_CROP
                                    }
                                },
                                modifier = modifier.fillMaxSize()
                            )
                            /*Image(
                                painter = painterResource(id = R.drawable.lesson_circle_background),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                            )

                             */
                            var isLoading by remember { mutableStateOf(true) }
                            var imagePainter: Painter? by remember { mutableStateOf(null) }

                            val painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(""/*ConfigData.BASE_URL + lesson["lesson_img_adr"]*/)
                                    .error(R.drawable.no_lesson_image)
                                    .listener(
                                        onStart = { isLoading = true },
                                        onSuccess = { _, _ -> isLoading = false },
                                        onError = { _, _ -> isLoading = false }
                                    )
                                    .build()
                            )
                            Box(
                                modifier = Modifier.size(120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(40.dp)
                                    )
                                } else {
                                    Image(
                                        painter = imagePainter ?: painter,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                    var colorForCV by remember { mutableStateOf("0xFF4CAF50") }
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(22.dp)
                            .offset(x = (-10).dp)
                            //.padding(horizontal = (-10).dp)
                            .background(Color(colorForCV.removePrefix("0x").toLong(16)), shape = MaterialTheme.shapes.medium),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(
                                    start = 10.dp,
                                    top = 4.dp,
                                    end = 10.dp,
                                    bottom = 4.dp
                                )
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(0.dp), // Убрана тень
                            shape = MaterialTheme.shapes.medium, // Задание скругленных углов
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

                        Text(
                            text = "${lesson["lesson_number"]}." + "\u00A0" + "${lesson["lesson_short_name"]}",
                            textAlign = TextAlign.Center,
                            modifier = modifier
                                .wrapContentHeight()
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            maxLines = 4,
                            fontSize = 13.sp,
                            overflow = TextOverflow.Ellipsis,
                            color = colorResource(id = R.color.lesson_text_color),
                            fontFamily = FontFamily(Font(R.font.montserrat_medium_new)),
                        )
                        lesson["status"]?.let {
                            colorForCV = lessonsRoadViewModel.getLessonsStatusColorById(it).toString()
                        }

                        if (previousItemViewType == LessonsRoadAdapter.VIEW_TYPE_LEFT) {
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
                    }
                }

                if (ivLineRight.value && ivLineRes.value != null) {
                    //id = ivForLine
                    Image(
                        modifier = modifier
                            .width(ivLineWidthRight.value.dp)
                            .height(ivLineHeightRight.value.dp)

                            .padding(bottom = 4.dp, start = (-10).dp)
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
    var marginsForFL = remember { mutableStateListOf(0,-50,25,0) }
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
    //try {
        Box (
            modifier = modifier
                //.padding(start = marginsForFL[0].dp, top = marginsForFL[1].dp, end = marginsForFL[2].dp, bottom = marginsForFL[3].dp)
                .padding(start = marginsForFL[0].dp, end = marginsForFL[2].dp, bottom = marginsForFL[3].dp)
                .fillMaxWidth()
                .offset(y = marginsForFL[1].dp)
                .wrapContentHeight()
                //Получение высоты после flRootLayout после постройки UI
                .onGloballyPositioned { layoutCoordinates ->
                    measuredHeight.value = layoutCoordinates.size.height
                }
        ) {
            Row (
                modifier = if (llHorizontalContainer.value == null) {
                    modifier
                        .fillMaxSize()
                        .wrapContentWidth(Alignment.End)
                } else {
                    modifier
                        .fillMaxWidth()
                        .height(llHorizontalContainer.value!!)
                        .wrapContentWidth(Alignment.End)
                }
            ){
                if (ivLineLeft.value != false && ivLineRes.value != null) {
                    Image(
                        modifier = modifier
                            .width(ivLineWidthLeft.value.dp)
                            .height(ivLineHeightLeft.value.dp)
                            .align(Alignment.CenterVertically)
                            .padding(end = (-10).dp)
                            .graphicsLayer {
                                rotationY = 0f
                            },
                        bitmap = ivLineRes.value!!.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Column (
                    modifier = modifier
                        .fillMaxHeight()
                        .clickable { onLessonClick(lesson) }
                        .width(130.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    Box (
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 10.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Card (
                            modifier = Modifier
                                .matchParentSize()
                                .align(Alignment.Center),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            shape = CircleShape,
                            elevation = CardDefaults.cardElevation(0.dp)
                        ){
                            AndroidView(
                                factory = { context ->
                                    ImageView(context).apply {
                                        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.lesson_circle_background))
                                        scaleType = ImageView.ScaleType.CENTER_CROP
                                    }
                                },
                                modifier = modifier.fillMaxSize()
                            )
                            /*Image(
                                painter = painterResource(id = R.drawable.lesson_circle_background),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()

                            )

                             */
                            var isLoading by remember { mutableStateOf(true) }
                            var imagePainter: Painter? by remember { mutableStateOf(null) }

                            val painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(""/*ConfigData.BASE_URL + lesson["lesson_img_adr"]*/)
                                    .error(R.drawable.no_lesson_image)
                                    .listener(
                                        onStart = { isLoading = true },
                                        onSuccess = { _, _ -> isLoading = false },
                                        onError = { _, _ -> isLoading = false }
                                    )
                                    .build()
                            )
                            Box(
                                modifier = Modifier.size(120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(40.dp)
                                    )
                                } else {
                                    Image(
                                        painter = imagePainter ?: painter,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                    var colorForCV by remember { mutableStateOf("0xFF4CAF50") }
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(22.dp)
                            //.padding(horizontal = -10.dp)
                            .offset(x = (-10).dp)
                            .background(Color(colorForCV.removePrefix("0x").toLong(16)), shape = MaterialTheme.shapes.medium),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(
                                    start = 10.dp,
                                    top = 4.dp,
                                    end = 10.dp,
                                    bottom = 4.dp
                                )
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(0.dp), // Убрана тень
                            shape = MaterialTheme.shapes.medium, // Задание скругленных углов
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

                        Text(
                            text = "${lesson["lesson_number"]}." + "\u00A0" + "${lesson["lesson_short_name"]}",
                            textAlign = TextAlign.Center,
                            modifier = modifier
                                .wrapContentHeight()
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            maxLines = 4,
                            fontSize = 13.sp,
                            overflow = TextOverflow.Ellipsis,
                            color = colorResource(id = R.color.lesson_text_color),
                            fontFamily = FontFamily(Font(R.font.montserrat_medium_new)),
                        )
                        lesson["status"]?.let {
                            colorForCV = lessonsRoadViewModel.getLessonsStatusColorById(it).toString()
                        }

                        if (previousItemViewType == LessonsRoadAdapter.VIEW_TYPE_LEFT) {
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
                    }
                }

                if (ivLineRight.value && ivLineRes.value != null) {
                    //id = ivForLine
                    Image(
                        modifier = modifier
                            .width(ivLineWidthRight.value.dp)
                            .height(ivLineHeightRight.value.dp)
                            .offset(x = (-10).dp)
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
    ivLineLeft:MutableState<Boolean>,
    ivLineWidth:MutableState<Int>,
    ivLineRes:MutableState<Bitmap?>,
    ivLineMargins:SnapshotStateList<Int>,
    ivLineRotationY:MutableState<Float>,
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
        marginsForFL.addAll(listOf(0,0,0,0))
        ivLineLeft.value = false
    } else {
        // Если это первый кружок или если это кружок справа, убираем отрицательный верхний отступ, иначе оставляем
        if (position == 0) {
            marginsForFL.clear()
            marginsForFL.addAll(listOf(0,0,0,0))
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
                marginsForFL.addAll(listOf(0,0,0,0))
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
                    ivLineMargins.addAll(listOf(0,0,0,0))
                } else {
                    ivLineWidth.value = 70
                    ivLineMargins.clear()
                    ivLineMargins.addAll(listOf(0,0,-lessonsViewModel.dpToPx(10)))
                }
                if (lineToLeft.value == null) {
                    Log.d("LOL", "width = ${ivLineWidth.value}")
                    lineToLeft.value = lessonsRoadViewModel.createLineBitmapRightToLeft(
                        path = circlePath,
                        paint = paint,
                        width = ivLineWidth.value,
                        height = ivLineHeight.value - lessonsViewModel.dpToPx(50)
                    )
                }
                ivLineRes.value = lineToLeft.value
            } else {
                ivLineRotationY.value = 0f
                if (isScreenLarge) ivLineWidth.value = lessonsViewModel.dpToPx(470)
                else {
                    ivLineWidth.value = lessonsViewModel.dpToPx(120)
                }
                if (lineToRight == null) {
                    lineToRight.value = lessonsRoadViewModel.createLineBitmapLeftToRight(
                        path = circlePath,
                        paint = paint,
                        width = ivLineWidth.value,
                        height = ivLineHeight.value - lessonsViewModel.dpToPx(50)
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
    ivLineMargins:SnapshotStateList<Int>,
    lineToRight: MutableState<Bitmap?>,
    lineToLeft: MutableState<Bitmap?>,
    circlePath: Path,
    paint: Paint,
    llHorizontalContainer: MutableState<Dp?>,
    ivLineRes:MutableState<Bitmap?>,
) {
    if (lessons.size == 1) {
        marginsForFL.clear()
        marginsForFL.addAll(listOf(0,0,0,0))
        ivLineRight.value = false
    } else {
        if (position != lessons.lastIndex) {
            ivLineRight.value = true
            if (position == 0) {
                marginsForFL.clear()
                marginsForFL.addAll(listOf(0,0,0,0))
            } else if (position % 2 != 0) {
                marginsForFL.clear()
                marginsForFL.addAll(listOf(0, -lessonsViewModel.dpToPx(50), 0, 0))
            } else {
                marginsForFL.clear()
                marginsForFL.addAll(listOf(0, -lessonsViewModel.dpToPx(130), 0, 0))
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
            if (lineToRight == null) {
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
                ivLineMargins.addAll(listOf(0,0,0,0))
                //Я не понял зачем в XML коде 2 раза ставились margin
                ivLineMargins.clear()
                ivLineMargins.addAll(listOf(0,0,0,0))
            } else {
                ivLineWidth.value = lessonsViewModel.dpToPx(70)
                ivLineMargins.clear()
                ivLineMargins.addAll(listOf(0,0,-lessonsViewModel.dpToPx(10),0))
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
        llHorizontalContainer.value = lessonsViewModel.dpToPx(240).dp
    }
}
@Preview
@Composable
fun LessonsRoadAdapterScreenPreview() {
    var lessonsViewModel = LessonsViewModel(LocalContext.current.applicationContext as Application)
    var lessonsRoadViewModel = LessonsRoadViewModel(LocalContext.current.applicationContext as Application)
    val roadlist = arrayListOf<Map<String, String>>()
    if (lessonsRoadViewModel.lessonsRoadList != null) {
        // Установка списка дорожки уроков
        for (road in lessonsRoadViewModel.lessonsRoadList!!) {
            roadlist.add(road)
        }
        lessonsRoadViewModel.groupedLessons =
            lessonsRoadViewModel.getLessonsByChapter(roadlist)
        lessonsRoadViewModel.groupedLessons.forEachIndexed { index, lessons ->
            LessonsRoadAdapterScreen(
                modifier = Modifier,
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




