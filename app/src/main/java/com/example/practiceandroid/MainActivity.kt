package com.example.practiceandroid

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.practiceandroid.modules.lessonsRoad.views.LessonsFragment
import com.example.practiceandroid.modules.lessonsRoad.views.LessonsRoadCompose
import com.example.practiceandroid.modules.lessonsRoad.views.LessonsRoadFragment
import com.example.practiceandroid.modules.lessonsRoad.views.VIEW_TYPE_LEFT
import com.example.practiceandroid.modules.lessonsRoad.views.VIEW_TYPE_RIGHT
import com.example.practiceandroid.theme.PracticeAndroidTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
//            PracticeAndroidTheme {
//                AndroidView(factory= {context->
//                    val fragmentContainer = FragmentContainerView(context)
//                    fragmentContainer.id = ViewCompat.generateViewId()
//                    (context as AppCompatActivity).supportFragmentManager.commit {
//                        replace<LessonsFragment>(fragmentContainer.id)
//                        setReorderingAllowed(true)
//                    }
//
//                    fragmentContainer
//                })
//            }
            LessonsRoadCompose(VIEW_TYPE_LEFT)
        }
    }
}