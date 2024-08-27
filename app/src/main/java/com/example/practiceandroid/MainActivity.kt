package com.example.practiceandroid

import MainViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practiceandroid.theme.PracticeAndroidTheme
import com.example.practiceandroid.views.DrawShapes


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = viewModel()

            //Для сброса фокуса ввода
            val focusManager = LocalFocusManager.current

            PracticeAndroidTheme(
            ) {
                Box (
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { focusManager.clearFocus() }
                ){
                    DrawShapes(mainViewModel.responseShapes.value, focusManager)
                }
            }
        }
    }
}