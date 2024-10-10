package com.example.practiceandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.practiceandroid.modules.webViewFix.PopupStandard
import com.example.practiceandroid.modules.webViewFix.WebViewScreen
import com.example.practiceandroid.theme.PracticeAndroidTheme
import com.example.practiceandroid.viewModels.MainViewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeAndroidTheme {
                PopupStandard(mainViewModel)
            }
        }
    }
}