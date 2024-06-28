package com.example.practiceandroid.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.practiceandroid.viewModels.PdfViewerViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PdfViewer(viewModel: PdfViewerViewModel = viewModel()) {

    val bitmap by viewModel.bitmap.observeAsState()
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(Unit) {
        viewModel.loadPdf("sample.pdf")
    }

    BoxWithConstraints {
        val state = rememberTransformableState { zoom, pan, ratation ->
            scale = (scale * zoom).coerceIn(1f, 3f)
            val extraWidth = (scale - 1) * maxWidth
            val extraHeight = (scale - 1) * maxHeight

            val maxX = extraWidth
            val maxY = extraHeight

            offset = Offset(
                x = (offset.x + scale * pan.x).coerceIn(-maxX.value, maxX.value),
                y = (offset.y + scale * pan.y).coerceIn(-maxY.value, maxY.value)
            )


        }
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .graphicsLayer {
                        scaleY = scale
                        scaleX = scale
                        translationY = offset.y
                        translationX = offset.x

                    }
                    .transformable(state)

            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Button(onClick = { viewModel.previousPage() }) {
                Text("Назад")
            }
            Button(onClick = { viewModel.nextPage() }) {
                Text("Вперед")
            }
        }
    }
}


@Preview
@Composable
fun PdfViewerPreview(){
    PdfViewer(viewModel())
}