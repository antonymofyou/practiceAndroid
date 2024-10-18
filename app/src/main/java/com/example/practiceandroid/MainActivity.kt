package com.example.practiceandroid

import android.os.Bundle
import android.text.Layout.Alignment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.HorizontalAlignmentLine
import com.example.practiceandroid.modules.transparentPictureFix.TransparentPicture
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
                TransparentPicture()
            }
        }
    }
}

