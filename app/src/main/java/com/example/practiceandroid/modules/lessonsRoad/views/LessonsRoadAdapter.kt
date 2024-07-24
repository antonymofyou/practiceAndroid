package com.example.practiceandroid.modules.lessonsRoad.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.practiceandroid.ConfigData
import com.example.practiceandroid.R
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsRoadViewModel
import com.example.practiceandroid.modules.lessonsRoad.viewModels.LessonsViewModel

// Адаптер для дорожки уроков
class LessonsRoadAdapter(
    private val context: Context,
    private val lessons: ArrayList<Map<String, String>>,
    private val isScreenLarge: Boolean,
    private val lessonsRoadViewModel: LessonsRoadViewModel,
    private val lessonsViewModel: LessonsViewModel,
    private val previousItemViewType: Int,
    private val isScrollAdapter: Boolean = false,
    // Слушатель нажатия по кружку урока
    private val onLessonClick: (lesson: Map<String, String>) -> Unit,
) :
    RecyclerView.Adapter<LessonsRoadAdapter.ViewHolder>() {

    // Константы для определения типа View
    companion object {
        val VIEW_TYPE_RIGHT = 0
        val VIEW_TYPE_LEFT = 1
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = lessonsRoadViewModel.getLineColorForChapter(lessons[0]["lesson_chapter"]!!)
    }

    private val circlePath = Path()
    private var lineToRight: Bitmap? = null
    private var lineToLeft: Bitmap? = null

    // Объявление ViewHolder'ов
    sealed class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val rootLayout = itemView.findViewById<FrameLayout>(R.id.flRootLayout)
        val llHorizontalContainer =
            rootLayout.findViewById<LinearLayout>(R.id.llHorizontalContainer)
        val ivForLine = llHorizontalContainer.findViewById<ImageView>(R.id.ivLine)
        val lessonContainer = rootLayout.findViewById<LinearLayout>(R.id.llLessonContainer)
        val cvLessonStatus = lessonContainer.findViewById<CardView>(R.id.cvLessonStatus)
        val ivLesson = lessonContainer.findViewById<CardView>(R.id.cvCircleImage)
            .findViewById<ImageView>(R.id.ivLesson)
        val ivLessonsImageProgress = lessonContainer.findViewById<ProgressBar>(R.id.progressImage)
        val lessonName = lessonContainer.findViewById<TextView>(R.id.tvLessonName)
        val lessonStatus = cvLessonStatus.findViewById<TextView>(R.id.tvLessonStatus)

        class RightViewHolder(itemView: View) : ViewHolder(itemView)
        class LeftViewHolder(itemView: View) : ViewHolder(itemView)
        class ErrorRoadViewHolder(itemView: View) : ViewHolder(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            // Рисуем View в зависимости от типа ViewHolder
            VIEW_TYPE_RIGHT -> ViewHolder.RightViewHolder(
                LayoutInflater.from(context).inflate(R.layout.view_type_right, parent, false)
            )
            VIEW_TYPE_LEFT -> ViewHolder.LeftViewHolder(
                LayoutInflater.from(context).inflate(R.layout.view_type_left, parent, false)
            )
            else -> ViewHolder.ErrorRoadViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.item_error_lessons_road, parent, false)
            )
        }
    }

    // Установка значений из массива во вью
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = lessons[position]
        try {
            // Очищаем картинку
            holder.ivLessonsImageProgress.visibility = View.VISIBLE
            holder.ivLesson.visibility = View.GONE
            holder.ivForLine.setImageDrawable(null)
            Glide.with(context)
                .load(""/*ConfigData.BASE_URL + lesson["lesson_img_adr"]*/)
                .error(R.drawable.no_lesson_image)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.ivLessonsImageProgress.visibility = View.GONE
                        holder.ivLesson.visibility = View.VISIBLE
                        holder.ivLesson.setImageResource(R.drawable.no_lesson_image)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.ivLessonsImageProgress.visibility = View.GONE
                        holder.ivLesson.visibility = View.VISIBLE
                        return false
                    }
                })
                .into(holder.ivLesson)
            holder.lessonName.text =
                "${lesson["lesson_number"]}." + "\u00A0" + "${lesson["lesson_short_name"]}"
            holder.lessonStatus.text = lesson["status"]?.let {
                lessonsRoadViewModel.setLessonStatusNameById(it)
            }
            lesson["status"]?.let {
                holder.cvLessonStatus.setCardBackgroundColor(
                    lessonsRoadViewModel.getLessonsStatusColorById(it)
                )
            }
            holder.lessonContainer.setOnClickListener {
                onLessonClick(lesson)
            }
            if (previousItemViewType == VIEW_TYPE_LEFT) {
                Log.d("LOL", "previousItemViewType == VIEW_TYPE_LEFT")
                setupLinesWhenFirstElementInRightSide(holder, position)
            } else {
                Log.d("LOL", "previousItemViewType == VIEW_TYPE_RIGHT")
                setupLinesWhenFirstElementInLeftSide(holder, position)
            }
            if (isScrollAdapter && lessonsRoadViewModel.firstUnfulfilledLesson!!["lesson_number"]!!.toInt() < lesson["lesson_number"]!!.toInt()) {
                holder.rootLayout.post {
                    lessonsRoadViewModel.scrollRecyclerHeightSumAfterScrollElement += holder.rootLayout.marginTop
                    lessonsRoadViewModel.scrollRecyclerHeightSumAfterScrollElement += holder.rootLayout.measuredHeight
                }
            }

        } catch (e: Exception) {
            Log.d("LessonRoadAdapter", e.message.toString())
        }
        circlePath.reset()
    }

    // Получение типа вью в зависимости от позиции
    override fun getItemViewType(position: Int): Int {
        return if (previousItemViewType == VIEW_TYPE_LEFT) {
            if (position % 2 == 0) VIEW_TYPE_RIGHT else VIEW_TYPE_LEFT
        } else {
            if (position % 2 == 0) VIEW_TYPE_LEFT else VIEW_TYPE_RIGHT
        }
    }

    // Получаем размер списка
    override fun getItemCount(): Int {
        return lessons.size
    }

    // Установка линий, когда первый элемент находиться на левой стороне
    private fun setupLinesWhenFirstElementInLeftSide(holder: ViewHolder, position: Int) {
        if (lessons.size == 1) {
            holder.rootLayout.updateLayoutParams<MarginLayoutParams> {
                setMargins(0, 0, 0, 0)
            }
            holder.ivForLine.visibility = View.GONE
        } else {
            // Если это первый кружок или если это кружок справа, убираем отрицательный верхний отступ, иначе оставляем
            if (position == 0) {
                holder.rootLayout.updateLayoutParams<MarginLayoutParams> {
                    setMargins(0, 0, 0, 0)
                }
            } else if (position % 2 != 0) {
                holder.rootLayout.updateLayoutParams<MarginLayoutParams> {
                    setMargins(0, -lessonsViewModel.dpToPx(130), 0, 0)
                }
            } else {
                holder.rootLayout.updateLayoutParams<MarginLayoutParams> {
                    setMargins(0, -lessonsViewModel.dpToPx(50), 0, 0)
                }
            }
            if (isScreenLarge) holder.ivForLine.updateLayoutParams<LinearLayout.LayoutParams> {
                width = lessonsViewModel.dpToPx(435)
            } else {
                holder.ivForLine.updateLayoutParams<LinearLayout.LayoutParams> {
                    width = lessonsViewModel.dpToPx(120)
                }
            }
            //здесь строится линия
            if (position != lessons.lastIndex) {
                holder.ivForLine.visibility = View.VISIBLE
                // Если это первый кружок или если это кружок справа, убираем отрицательный верхний отступ, иначе оставляем
                if (position == 0) {
                    holder.rootLayout.updateLayoutParams<MarginLayoutParams> {
                        setMargins(0, 0, 0, 0)
                    }
                } else if (position % 2 == 0) {
                    holder.rootLayout.updateLayoutParams<MarginLayoutParams> {
                        setMargins(0, -lessonsViewModel.dpToPx(50), 0, 0)
                    }
                } else {
                    holder.rootLayout.updateLayoutParams<MarginLayoutParams> {
                        setMargins(0, -lessonsViewModel.dpToPx(130), 0, 0)
                    }
                }
                if (position % 2 == 0 && position != 0) {
                    holder.rootLayout.updateLayoutParams<MarginLayoutParams> {
                        setMargins(0, -lessonsViewModel.dpToPx(50), 0, 0)
                    }
                }
                // Рисуем линии
                if (position % 2 == 0) {
                    holder.ivForLine.rotationY = 180f
                    if (isScreenLarge) {
                        holder.ivForLine.updateLayoutParams<LinearLayout.LayoutParams> {
                            width = lessonsViewModel.dpToPx(435)
                        }
                        holder.ivForLine.updateLayoutParams<MarginLayoutParams> {
                            setMargins(0, 0, 0, 0)
                        }
                    } else {
                        holder.ivForLine.updateLayoutParams<LinearLayout.LayoutParams> {
                            width = lessonsViewModel.dpToPx(70)
                        }
                        holder.ivForLine.updateLayoutParams<MarginLayoutParams> {
                            setMargins(0, 0, -lessonsViewModel.dpToPx(10), 0)
                        }
                    }
                    if (lineToLeft == null) {
                        Log.d("LOL", "ivLine height = ${holder.ivForLine.layoutParams.height}, width = ${holder.ivForLine.layoutParams.width}, dpToDx = ${lessonsViewModel.dpToPx(50)}")
                        lineToLeft = lessonsRoadViewModel.createLineBitmapRightToLeft(
                            path = circlePath,
                            paint = paint,
                            width = holder.ivForLine.layoutParams.width,
                            height = holder.ivForLine.layoutParams.height
                        )
                    }
                    holder.ivForLine.setImageBitmap(lineToLeft)
                } else {
                    holder.ivForLine.rotationY = 0f
                    if (isScreenLarge) holder.ivForLine.updateLayoutParams<LinearLayout.LayoutParams> {
                        width = lessonsViewModel.dpToPx(470)
                    } else {
                        holder.ivForLine.updateLayoutParams<LinearLayout.LayoutParams> {
                            width = lessonsViewModel.dpToPx(120)
                        }
                    }
                    if (lineToRight == null) {
                        lineToRight = lessonsRoadViewModel.createLineBitmapLeftToRight(
                            path = circlePath,
                            paint = paint,
                            width = holder.ivForLine.layoutParams.width,
                            height = holder.ivForLine.layoutParams.height - lessonsViewModel.dpToPx(
                                50
                            )
                        )
                    }
                    holder.ivForLine.setImageBitmap(lineToRight)
                }
            }
        }
        if (position == lessons.lastIndex) {
            holder.llHorizontalContainer.updateLayoutParams<FrameLayout.LayoutParams> {
                height = FrameLayout.LayoutParams.WRAP_CONTENT
            }
            holder.ivForLine.visibility = View.GONE
        } else {
            holder.ivForLine.visibility = View.VISIBLE
            holder.llHorizontalContainer.updateLayoutParams<FrameLayout.LayoutParams> {
                height = lessonsViewModel.dpToPx(240)
            }
        }
    }

    // Установка линий, когда первый элемент находиться на правой стороне
    private fun setupLinesWhenFirstElementInRightSide(holder: ViewHolder, position: Int) {
        if (lessons.size == 1) {
            holder.rootLayout.updateLayoutParams<MarginLayoutParams> {
                setMargins(0, 0, 0, 0)
            }
            holder.ivForLine.visibility = View.GONE
        } else {
            if (position != lessons.lastIndex) {
                holder.ivForLine.visibility = View.VISIBLE
                // Если это первый кружок или если это кружок справа, убираем отрицательный верхний отступ, иначе оставляем
                if (position == 0) {
                    holder.rootLayout.updateLayoutParams<MarginLayoutParams> {
                        setMargins(0, 0, 0, 0)
                    }
                } else if (position % 2 != 0) {
                    holder.rootLayout.updateLayoutParams<MarginLayoutParams> {
                        setMargins(0, -lessonsViewModel.dpToPx(50), 0, 0)
                    }
                } else {
                    holder.rootLayout.updateLayoutParams<MarginLayoutParams> {
                        setMargins(0, -lessonsViewModel.dpToPx(130), 0, 0)
                    }
                }
            } else {
                holder.ivForLine.visibility = View.GONE
            }
            // Рисуем линии
            if (position % 2 == 0) {
                holder.ivForLine.rotationY = 0f
                if (isScreenLarge) {
                    holder.ivForLine.updateLayoutParams<LinearLayout.LayoutParams> {
                        width = lessonsViewModel.dpToPx(470)
                    }
                } else {
                    holder.ivForLine.updateLayoutParams<LinearLayout.LayoutParams> {
                        width = lessonsViewModel.dpToPx(120)
                    }
                }
                if (lineToRight == null) {
                    Log.d("LOL", "ivLine height = ${holder.ivForLine.layoutParams.height}, width = ${holder.ivForLine.layoutParams.width}, dpToDx = ${lessonsViewModel.dpToPx(50)}")
                    lineToRight = lessonsRoadViewModel.createLineBitmapLeftToRight(
                        path = circlePath,
                        paint = paint,
                        width = holder.ivForLine.layoutParams.width,
                        height = holder.ivForLine.layoutParams.height - lessonsViewModel.dpToPx(50)
                    )
                }
                holder.ivForLine.setImageBitmap(lineToRight)
            } else {
                holder.ivForLine.rotationY = 180f
                if (isScreenLarge) holder.ivForLine.updateLayoutParams<LinearLayout.LayoutParams> {
                    width = lessonsViewModel.dpToPx(435)
                    holder.ivForLine.updateLayoutParams<MarginLayoutParams> {
                        setMargins(0, 0, 0, 0)
                    }
                    holder.ivForLine.updateLayoutParams<MarginLayoutParams> {
                        setMargins(0, 0, 0, 0)
                    }
                } else {
                    holder.ivForLine.updateLayoutParams<LinearLayout.LayoutParams> {
                        width = lessonsViewModel.dpToPx(70)
                    }
                    holder.ivForLine.updateLayoutParams<MarginLayoutParams> {
                        setMargins(0, 0, -lessonsViewModel.dpToPx(10), 0)
                    }
                }
                if (lineToLeft == null) {
                    lineToLeft = lessonsRoadViewModel.createLineBitmapRightToLeft(
                        path = circlePath,
                        paint = paint,
                        width = holder.ivForLine.layoutParams.width,
                        height = holder.ivForLine.layoutParams.height
                    )
                }
                holder.ivForLine.setImageBitmap(lineToLeft)
            }
        }
        if (position == lessons.lastIndex) {
            holder.llHorizontalContainer.updateLayoutParams<FrameLayout.LayoutParams> {
                height = FrameLayout.LayoutParams.WRAP_CONTENT
            }
            holder.ivForLine.visibility = View.GONE
        } else {
            holder.ivForLine.visibility = View.VISIBLE
            holder.llHorizontalContainer.updateLayoutParams<FrameLayout.LayoutParams> {
                height = lessonsViewModel.dpToPx(240)
            }
        }
    }
}