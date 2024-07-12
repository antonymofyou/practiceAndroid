package com.example.practiceandroid.modules.lessonsRoad.views

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practiceandroid.R
import com.example.practiceandroid.databinding.FragmentLessonsRoadBinding
import com.example.practiceandroid.databinding.FragmentTestAdapterBinding
import com.example.practiceandroid.databinding.ItemExternalLessonsRoadBinding
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonStatus
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadViewModel
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TestAdapterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TestAdapterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentTestAdapterBinding
    val lessonsRoadViewModel: LessonsRoadViewModel by viewModels()
    val lessonsViewModel: LessonsViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#27A4FF")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTestAdapterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val roadlist = arrayListOf<Map<String, String>>()
        if (lessonsRoadViewModel.lessonsRoadList != null) {
            // Установка списка дорожки уроков
            for (road in lessonsRoadViewModel.lessonsRoadList!!) {
                roadlist.add(road)
            }
            lessonsRoadViewModel.groupedLessons =
                lessonsRoadViewModel.getLessonsByChapter(roadlist)

            // В зависимости от ширины экрана устанавливаем размеры дорожки уроков
            lessonsViewModel.isScreenLarge =
                lessonsViewModel.pxToDp(requireContext().resources.displayMetrics.widthPixels) >= 730

            lessonsRoadViewModel.groupedLessons.forEachIndexed { index, lessons ->
                binding.rvRoot.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = LessonsRoadAdapter(
                        requireContext(),
                        lessons,
                        lessonsViewModel.isScreenLarge,
                        lessonsRoadViewModel,
                        lessonsViewModel,
                        lessonsRoadViewModel.getGroupedLessonsWithViewType(
                            lessonsRoadViewModel.groupedLessons
                        )[index].viewType,
                        lessons.contains(lessonsRoadViewModel.firstUnfulfilledLesson)
                    ) { lesson ->
                        if (lesson["status"] != "0") {
                            lessonsViewModel.currentLesson.value = lesson
                            lessonsViewModel.lessonStatus.value =
                                LessonStatus.LESSON
                            lessonsViewModel.currentLessonNum.value =
                                lesson["lesson_number"]
                        }
                    }
                }
            }
        }
    }

}