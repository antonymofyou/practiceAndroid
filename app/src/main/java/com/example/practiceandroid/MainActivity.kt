package com.example.practiceandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practiceandroid.theme.PracticeAndroidTheme
import com.example.practiceandroid.viewModels.MainViewModel
import com.example.practiceandroid.views.DrawShapes


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = viewModel()

            PracticeAndroidTheme(
            ) {

                Box (
                    modifier = Modifier.height(300.dp).width(300.dp)
                ){
                    DrawShapes(mainViewModel.responseShapes.value)
                }
            }
        }
    }
}