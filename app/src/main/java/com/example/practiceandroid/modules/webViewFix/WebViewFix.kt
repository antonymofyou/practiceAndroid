package com.example.practiceandroid.modules.webViewFix

import android.view.MotionEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.wear.compose.material.ContentAlpha
import androidx.wear.compose.material.LocalContentAlpha
import androidx.wear.compose.material.MaterialTheme.colors
import com.example.practiceandroid.R
import com.example.practiceandroid.viewModels.MainViewModel
import com.example.practiceandroid.viewModels.StandardsPopupStatus

@Composable
fun PopupStandard(viewModel: MainViewModel) {
    val uriHandler = LocalUriHandler.current

    val isScrollingEnabled = remember { mutableStateOf(true) }

    val scrollState = rememberScrollState()

    NoPaddingAlertDialog(
        modifier = Modifier
            .padding(20.dp)
            .border(
                BorderStroke(
                    1.dp,
                    colors.background
                ),
                shape = RoundedCornerShape(25.dp)
            )
            .clip(RoundedCornerShape(25.dp))
            .verticalScroll(scrollState, isScrollingEnabled.value)
            .fillMaxWidth(),
        backgroundColor = Color.LightGray,
        onDismissRequest = {
            viewModel.isShowPopUp.value = false
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = {
            Box(modifier = Modifier.padding(end = 24.dp, top = 24.dp)) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = colors.background)
                        .border(
                            width = 1.dp,
                            color = colors.primary,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .height(32.dp)
                        .width(32.dp)
                        .fillMaxWidth()
                        .clickable {
                            viewModel.isShowPopUp.value = false
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(18.dp)
                    )
                }
                Column {
                    viewModel.standard["name"]?.let { name ->
                        Text(
                            text = name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 35.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    viewModel.standard["process"]?.let { process ->
                        Text(
                            text = "($process)",
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.secondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
            ) {
                viewModel.standard["id"]?.let { id ->
                    Text(
                        modifier = Modifier.padding(start = 24.dp),
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle()) {
                                append("id: ")
                            }
                            withStyle(style = SpanStyle(color = colors.secondary)) {
                                append(id)
                            }
                        }
                    )
                }
                viewModel.standard["link"]?.let { link ->
                    val annotatedString = buildAnnotatedString {
                        pushStringAnnotation(tag = "URL", annotation = link)
                        withStyle(
                            style = SpanStyle()
                        ) {
                            append("Ссылка")
                        }
                    }
                    ClickableText(
                        modifier = Modifier.padding(start = 24.dp),
                        text = annotatedString,
                        onClick = {
                            annotatedString.getStringAnnotations(
                                tag = "URL",
                                start = it,
                                end = it
                            )
                                .firstOrNull()?.let { link ->
                                    uriHandler.openUri(link.item)
                                }
                        }
                    )
                    WebViewScreen(link, isScrollingEnabled)
                }
                Spacer(modifier = Modifier.height(12.dp))
                viewModel.standard["updatedAt"]?.let { updatedAt ->
                    if (viewModel.standard["updatedAt"]?.isNotEmpty() == true) {
                        Text(

                            text = "Изменен: $updatedAt",
                            modifier = Modifier
                                .padding(start = 24.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                        )
                    }
                }
                viewModel.standard["learnedAt"]?.let { learnedAt ->
                    if (viewModel.standard["learnedAt"]?.isNotEmpty() == true) {
                        Text(
                            modifier = Modifier.padding(start = 24.dp),
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Color.Black)) {
                                    append("Изучен: ")
                                }
                                withStyle(style = SpanStyle(color = Color.Green)) {
                                    append(learnedAt)
                                }
                            },
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                viewModel.standard["learnedComment"]?.let { learnedComment ->
                    Spacer(modifier = Modifier.height(12.dp))
                    if (viewModel.standard["learnedComment"]?.isNotEmpty() == true) {
                        Text(
                            text = "Как я понял изменения: $learnedComment",
                            modifier = Modifier
                                .padding(start = 24.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (viewModel.popUpState.value == StandardsPopupStatus.LOADED) {
                Column {
                    viewModel.standard["isLearned"]?.let { isLearned ->
                        if (isLearned == "0") {
                            ButtonRectangle(
                                onClick = {
                                    // standardsViewModel.isShowPopUpComment.value = true
                                },
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .padding(bottom = 20.dp)
                            ) {
                                Text(
                                    text = "Отметить изученным",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun WebViewScreen(url: String, isScrollingEnabled: MutableState<Boolean>) {
    val iframe = """
    <html>
    <body>
        <iframe 
        src="https://docs.google.com/presentation/d/1vZc3SKGWP_s8nocHDv_S_q5O33WoIknek879pUcFMwE/preview"
         width="100%"
         height="580px">
        </iframe>
    </body>
    </html>
    """

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(end = 1.dp),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                settings.builtInZoomControls = true
                settings.displayZoomControls = false

                this.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        return false
                    }
                }
                loadData(iframe, "text/html", "UTF-8")

                var lastTouchX: Float = 0f
                var lastTouchY: Float = 0f

                setOnTouchListener { _, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            // Сохраняем начальные координаты при нажатии
                            lastTouchX = event.x
                            lastTouchY = event.y
                        }
                        MotionEvent.ACTION_MOVE -> {
                            // Вычисляем смещение по осям X и Y
                            val deltaX = event.x - lastTouchX
                            val deltaY = event.y - lastTouchY

                            if (Math.abs(deltaY) > Math.abs(deltaX)){
                                isScrollingEnabled.value = true
                            }
                            else {
                                isScrollingEnabled.value = false
                            }

                            // Обновляем последние координаты
                            lastTouchX = event.x
                            lastTouchY = event.y
                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            isScrollingEnabled.value = true
                        }
                    }
                    false
                }
            }
        }
    )
}

@Composable
fun NoPaddingAlertDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    backgroundColor: Color = Color.LightGray,
    contentColor: Color = contentColorFor(backgroundColor),
    properties: DialogProperties = DialogProperties()
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Surface(
            modifier = modifier,
            shape = shape,
            color = backgroundColor,
            contentColor = contentColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                title?.let {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                        val textStyle = MaterialTheme.typography.displayMedium
                        ProvideTextStyle(textStyle, it)
                    }
                }
                text?.let {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                        val textStyle = MaterialTheme.typography.displayMedium
                        ProvideTextStyle(textStyle, it)
                    }
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        dismissButton?.invoke()
                        confirmButton()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun NoPaddingAlertDialogPreview() {
    NoPaddingAlertDialog(
        onDismissRequest = {
        },
        title = {
            Text(text = "Заголовок диалога")
        },
        text = {
            Text(text = "Пример текста внутри диалога.")
        },
        confirmButton = {
            Button(onClick = {}) {
                Text("Подтвердить")
            }
        }
    )
}

@Composable
fun ButtonRectangle(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    backgroundColor: Color = Color.Gray,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
//            .fillMaxWidth()
            ,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor, contentColor = Color.LightGray ),
        contentPadding = PaddingValues(vertical = 13.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        enabled = enabled
    ) {
        Box(
//            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonRectanglePreview() {
    ButtonRectangle(
        modifier = Modifier.padding(20.dp),
        onClick = { }
    ) {
        Text(text = "Кнопка")
    }
}