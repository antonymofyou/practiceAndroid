package com.example.practiceandroid.modules.lessonsRoad.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.practiceandroid.R
import com.example.practiceandroid.databinding.FragmentLessonsBinding


// Родительский фрагмент для дорожки уроков
class LessonsFragment : Fragment() {

    var _binding: FragmentLessonsBinding? = null
    private val myBindClass get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLessonsBinding.inflate(inflater, container, false)
        return myBindClass.root
    }

    override fun onStart() {
        super.onStart()

        // Установка наблюдателей
        setObservers()
    }

    private fun setObservers() {
        // Переключаемся по фрагментам в зависимости от статуса страницы

                        childFragmentManager.commit {
                            replace<LessonsRoadFragment>(R.id.fragmentContainerLessons)
                            setReorderingAllowed(true)
                            //addToBackStack(null)
                        }


    }

}