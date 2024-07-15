package com.example.practiceandroid.modules.lessonsRoad.views

import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.LayoutParams
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.updateLayoutParams
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

            lessonsRoadViewModel.groupedLessons.forEachIndexed{index, lessons ->
                val itemExternalLesssonsRoadBinding =
                    ItemExternalLessonsRoadBinding.inflate(LayoutInflater.from(context))
                itemExternalLesssonsRoadBinding.rvRoot.apply {
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

                //Устанавливаем цвета картинкам параллакса в зависимости от раздела
                itemExternalLesssonsRoadBinding.parallaxBackgroundImage.clearColorFilter()
                val color = lessonsRoadViewModel.getParallaxImageColorForChapter(
                    lessons[0]["lesson_chapter"] ?: "")
                val colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
                itemExternalLesssonsRoadBinding.parallaxBackgroundImage.colorFilter = colorFilter
                //тут проблема пост делается когда view уже создано
                if (lessons.contains(lessonsRoadViewModel.firstUnfulfilledLesson)) {
                    itemExternalLesssonsRoadBinding.clRoot.post {
                        lessonsRoadViewModel.toScrollRvHeight =
                            itemExternalLesssonsRoadBinding.clRoot.measuredHeight
                    }
                    lessonsRoadViewModel.scrollToIndex = index + 2
                    lessonsRoadViewModel.scrollToLesson =
                        lessonsRoadViewModel.firstUnfulfilledLesson!!
                    lessonsRoadViewModel.scrollToLessons = lessons
                }
                itemExternalLesssonsRoadBinding.clRoot.post {
                    val chapterPosition =
                        lessonsRoadViewModel.getChapterPositionByLessonNumber(
                            lessonsRoadViewModel.firstUnfulfilledLesson!!["lesson_number"].toString(),
                            lessonsRoadViewModel.groupedLessons
                        )
                    if (index <= chapterPosition) {
                        lessonsRoadViewModel.elementsAfterFirstUnfulfilledLessonHeightSum += itemExternalLesssonsRoadBinding.clRoot.measuredHeight
                    }
                }
                binding.flRootLessonsRoad.addView(itemExternalLesssonsRoadBinding.root)
                itemExternalLesssonsRoadBinding.ivLineBottomLeft.visibility =
                    View.INVISIBLE
                itemExternalLesssonsRoadBinding.ivLineBottomRight.visibility =
                    View.INVISIBLE
                itemExternalLesssonsRoadBinding.ivLineTopLeft.visibility =
                    View.INVISIBLE
                itemExternalLesssonsRoadBinding.ivLineTopRight.visibility =
                    View.INVISIBLE

                val groupedLessonsWithViewType =
                    lessonsRoadViewModel.getGroupedLessonsWithViewType(
                        lessonsRoadViewModel.groupedLessons
                    )

                val chapterLessons = groupedLessonsWithViewType[index]
                val lessonChapter =
                    chapterLessons.lessons[0]["lesson_chapter"] ?: "Неизвестный раздел"

                itemExternalLesssonsRoadBinding.clRoot.setBackgroundColor(
                    lessonsRoadViewModel.getBackgroundColorForChapter(lessonChapter)
                )

                itemExternalLesssonsRoadBinding.cvChapterBottom.setCardBackgroundColor(
                    lessonsRoadViewModel.getColorForChapterCardView(lessonChapter)
                )
                itemExternalLesssonsRoadBinding.cvChapterTop.setCardBackgroundColor(
                    lessonsRoadViewModel.getColorForChapterCardView(lessonChapter)
                )
                itemExternalLesssonsRoadBinding.tvChapterBottom.text = lessonChapter
                itemExternalLesssonsRoadBinding.tvChapterTop.text = lessonChapter

                if (lessonsViewModel.isScreenLarge) {
                    setupLessonsRoadForLargeScreen(itemExternalLesssonsRoadBinding)
                } else {
                    setupLessonsRoadForSmallScreen(itemExternalLesssonsRoadBinding)
                }

                // Устанавливаем линию раздела снизу
                ///Делает линию от последнего эл. до кард и до след. раздела
                if (index != groupedLessonsWithViewType.lastIndex) {
                    paint.color =
                        lessonsRoadViewModel.getLineColorForChapter(lessons[0]["lesson_chapter"]!!)
                    setBottomLines(
                        itemExternalLesssonsRoadBinding,
                        index,
                        groupedLessonsWithViewType
                    )
                } else {
                    hideBottomLines(itemExternalLesssonsRoadBinding)
                    // Устанавливаем для первого раздела отступ снизу,
                    // чтобы не было белого фона по углам меню и раздел отображался корректно
                    itemExternalLesssonsRoadBinding.clRoot.setPadding(
                        0, 0, 0, resources.getDimension(R.dimen.height_bottom_menu).toInt() + lessonsViewModel.dpToPx(24)
                    )
                    // Делаем, чтобы фон расстягивался на весь экран, не оставляя пустоты
                    itemExternalLesssonsRoadBinding.clRoot.layoutParams = LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT
                    )
                    // Убираем привязку нижнего названия раздела, чтобы он не расползался
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(itemExternalLesssonsRoadBinding.clRoot)
                    constraintSet.clear(R.id.cvChapterBottom, ConstraintSet.BOTTOM)
                    constraintSet.applyTo(itemExternalLesssonsRoadBinding.clRoot)
                }
                // Устанавливаем линию раздела сверху
                if (index != 0) {
                    paint.color =
                        lessonsRoadViewModel.getLineColorForChapter(lessons[0]["lesson_chapter"]!!)
                    setTopLines(
                        itemExternalLesssonsRoadBinding,
                        index,
                        groupedLessonsWithViewType
                    )
                } else {
                    hideTopLines(itemExternalLesssonsRoadBinding)
                }

                itemExternalLesssonsRoadBinding.rvRoot.apply {
                    if (lessonsViewModel.isScreenLarge) {
                        updateLayoutParams<ConstraintLayout.LayoutParams> {
                            width = lessonsViewModel.dpToPx(730)
                        }
                    } else {
                        updateLayoutParams<ConstraintLayout.LayoutParams> {
                            width = lessonsViewModel.dpToPx(365)
                        }
                    }
                    updateLayoutParams<ConstraintLayout.LayoutParams> {
                        height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                    }
                }
            }
        }
        /*val roadlist = arrayListOf<Map<String, String>>()
        if (lessonsRoadViewModel.lessonsRoadList != null) {
            // Установка списка дорожки уроков
            for (road in lessonsRoadViewModel.lessonsRoadList!!) {
                roadlist.add(road)
            }
            //Log.d("LOL", "roadlist = ${roadlist.toString()}")
            lessonsRoadViewModel.groupedLessons =
                lessonsRoadViewModel.getLessonsByChapter(roadlist)
            // В зависимости от ширины экрана устанавливаем размеры дорожки уроков
            lessonsViewModel.isScreenLarge =
                lessonsViewModel.pxToDp(requireContext().resources.displayMetrics.widthPixels) >= 730

            lessonsRoadViewModel.groupedLessons.forEachIndexed { index, lessons ->
                Log.d("LOL", "lessons = $lessons")

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
        }*/
    }
    // Установка линии сверху
    private fun setTopLines(
        binding: ItemExternalLessonsRoadBinding,
        position: Int,
        groupedLessonsWithViewType: ArrayList<GroupedLessonsWithViewType>
    ) {
        circlePath.reset()
        if (groupedLessonsWithViewType[position].viewType == LessonsRoadAdapter.VIEW_TYPE_LEFT) {
            binding.ivLineTopRight.rotationX = 180f
            binding.ivLineTopLeft.rotationX = 180f
            binding.ivLineTopLeft.visibility = View.VISIBLE
            topLeftBitmap = lessonsRoadViewModel.createLineBetweenChapters(
                circlePath,
                paint,
                binding.ivLineTopLeft.layoutParams.width,
                binding.ivLineTopLeft.layoutParams.height
            )
            binding.ivLineTopLeft.setImageBitmap(topLeftBitmap)
            binding.ivLineTopRight.visibility = View.INVISIBLE
        } else {
            binding.ivLineTopRight.rotationX = 0f
            binding.ivLineTopLeft.rotationX = 0f
            binding.ivLineTopRight.visibility = View.VISIBLE
            topRightBitmap = lessonsRoadViewModel.createLineBetweenChapters(
                circlePath,
                paint,
                binding.ivLineTopRight.layoutParams.width,
                binding.ivLineTopRight.layoutParams.height
            )
            binding.ivLineTopRight.setImageBitmap(topRightBitmap)
            binding.ivLineTopLeft.visibility = View.INVISIBLE
        }
    }

    // Установка линии снизу
    private fun setBottomLines(
        binding: ItemExternalLessonsRoadBinding,
        position: Int,
        groupedLessonsWithViewType: ArrayList<GroupedLessonsWithViewType>
    ) {
        circlePath.reset()
        if (groupedLessonsWithViewType[position + 1].viewType == LessonsRoadAdapter.VIEW_TYPE_LEFT) {
            binding.ivLineBottomRight.rotationX = 180f
            binding.ivLineBottomRight.visibility = View.VISIBLE
            bottomRightBitmap = lessonsRoadViewModel.createLineBetweenChapters(
                circlePath,
                paint,
                binding.ivLineBottomRight.layoutParams.width,
                binding.ivLineBottomRight.layoutParams.height
            )
            binding.ivLineBottomRight.setImageBitmap(bottomRightBitmap)
            binding.ivLineBottomLeft.visibility = View.INVISIBLE
        } else {
            binding.ivLineBottomRight.rotationX = 0f
            binding.ivLineBottomLeft.visibility = View.VISIBLE
            bottomLeftBitmap = lessonsRoadViewModel.createLineBetweenChapters(
                circlePath,
                paint,
                binding.ivLineBottomLeft.layoutParams.width,
                binding.ivLineBottomLeft.layoutParams.height
            )
            binding.ivLineBottomLeft.setImageBitmap(bottomLeftBitmap)
            binding.ivLineBottomRight.visibility = View.INVISIBLE
        }
    }

    // Скрываем линии
    private fun hideBottomLines(binding: ItemExternalLessonsRoadBinding) {
        binding.ivLineBottomRight.visibility = View.GONE
        binding.ivLineBottomLeft.visibility = View.GONE
    }

    // Скрываем линии
    private fun hideTopLines(binding: ItemExternalLessonsRoadBinding) {
        binding.ivLineTopRight.visibility = View.GONE
        binding.ivLineTopLeft.visibility = View.GONE
    }

    // Установка дорожки уроков для больших экранов
    private fun setupLessonsRoadForLargeScreen(binding: ItemExternalLessonsRoadBinding) {
        binding.ivLineTopLeft.updateLayoutParams<ConstraintLayout.LayoutParams> {
            width = lessonsViewModel.dpToPx(274)
            height = lessonsViewModel.dpToPx(113)
        }
        binding.ivLineTopLeft.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(lessonsViewModel.dpToPx(95.6f), -lessonsViewModel.dpToPx(6), 0, 0)
        }
        binding.ivLineTopRight.updateLayoutParams<ConstraintLayout.LayoutParams> {
            width = lessonsViewModel.dpToPx(250)
            height = lessonsViewModel.dpToPx(115)
        }
        binding.ivLineTopRight.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(0, lessonsViewModel.dpToPx(10), lessonsViewModel.dpToPx(102), 0)
        }
        binding.ivLineBottomLeft.updateLayoutParams<ConstraintLayout.LayoutParams> {
            width = lessonsViewModel.dpToPx(288)
            height = lessonsViewModel.dpToPx(115)
        }
        binding.ivLineBottomLeft.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(0, 0, 0, -lessonsViewModel.dpToPx(5.5f))
        }
        binding.ivLineBottomRight.updateLayoutParams<ConstraintLayout.LayoutParams> {
            width = lessonsViewModel.dpToPx(276.5f)
            height = lessonsViewModel.dpToPx(115)
        }
        binding.ivLineBottomRight.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(0, 0, 0, -lessonsViewModel.dpToPx(2))
        }
    }

    private fun setupLessonsRoadForSmallScreen(binding: ItemExternalLessonsRoadBinding) {
        binding.ivLineTopLeft.updateLayoutParams<ConstraintLayout.LayoutParams> {
            width = lessonsViewModel.dpToPx(100)
        }
        binding.ivLineTopLeft.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(lessonsViewModel.dpToPx(84), -lessonsViewModel.dpToPx(19), 0, 0)
        }
        binding.ivLineTopRight.updateLayoutParams<ConstraintLayout.LayoutParams> {
            width = lessonsViewModel.dpToPx(95)
            height = lessonsViewModel.dpToPx(105)
        }
        binding.ivLineTopRight.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(0, 0, lessonsViewModel.dpToPx(90), 0)
        }
        binding.ivLineBottomLeft.updateLayoutParams<ConstraintLayout.LayoutParams> {
            width = lessonsViewModel.dpToPx(90)
            height = lessonsViewModel.dpToPx(105)
        }
        binding.ivLineBottomLeft.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(0, 0, 0, -lessonsViewModel.dpToPx(2))
        }
        binding.ivLineBottomRight.updateLayoutParams<ConstraintLayout.LayoutParams> {
            width = lessonsViewModel.dpToPx(100)
            height = lessonsViewModel.dpToPx(112)
        }
        binding.ivLineBottomRight.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(0, 0, 0, -lessonsViewModel.dpToPx(2))
        }
    }
}