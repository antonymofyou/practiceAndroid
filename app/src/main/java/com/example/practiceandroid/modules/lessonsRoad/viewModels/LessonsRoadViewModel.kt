package com.example.practiceandroid.modules.lessonsRoad.viewModels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.practiceandroid.R
import com.example.practiceandroid.modules.lessonsRoad.views.GroupedLessonsWithViewType
import com.example.practiceandroid.modules.lessonsRoad.views.LessonsRoadAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.math.pow
import kotlin.math.sqrt

class LessonsRoadViewModel(private val app: Application) : AndroidViewModel(app) {

    val jsonStringData = """
[
    {"lesson_number":"39","lesson_name":"Политические партии","lesson_short_name":"Политические партии","lesson_chapter":"Политика","lesson_img_adr":"\/images\/lessons\/39.png","status":"3"},
    {"lesson_number":"38","lesson_name":"Правовое государство и гражданское общество.","lesson_short_name":"Правовое государство","lesson_chapter":"Политика","lesson_img_adr":"\/images\/lessons\/38.png","status":"2"},
    {"lesson_number":"37","lesson_name":"Политический режим","lesson_short_name":"Политический режим","lesson_chapter":"Политика","lesson_img_adr":"\/images\/lessons\/37.png","status":"2"},
    {"lesson_number":"36","lesson_name":"Государство и его функции. Формы государства.","lesson_short_name":"Государство ","lesson_chapter":"Политика","lesson_img_adr":"\/images\/lessons\/36.png","status":"2"},
    {"lesson_number":"35","lesson_name":"Политическая система общества.Политические институты.","lesson_short_name":"Политическая система общества","lesson_chapter":"Политика","lesson_img_adr":"\/images\/lessons\/35.png","status":"3"},
    {"lesson_number":"34","lesson_name":"Политика и власть","lesson_short_name":"Политика","lesson_chapter":"Политика","lesson_img_adr":"\/images\/lessons\/34.png","status":"2"},
    {"lesson_number":"33","lesson_name":"Практика по социологии","lesson_short_name":"Практика по социологии","lesson_chapter":"Социология","lesson_img_adr":"\/images\/lessons\/33.png","status":"2"},
    {"lesson_number":"32","lesson_name":"Семья","lesson_short_name":"Семья","lesson_chapter":"Социология","lesson_img_adr":"\/images\/lessons\/32.png","status":"2"},
    {"lesson_number":"31","lesson_name":"Этнические общности. Межнациональные отношения.","lesson_short_name":"Этнические общности","lesson_chapter":"Социология","lesson_img_adr":"\/images\/lessons\/31.png","status":"2"},
    {"lesson_number":"30","lesson_name":"Виды социальных норм. Социальный контроль. Отклоняющееся поведение и его типы.","lesson_short_name":"Социальный контроль","lesson_chapter":"Социология","lesson_img_adr":"\/images\/lessons\/30.png","status":"2"},
    {"lesson_number":"29","lesson_name":"Социальная стратификация и мобильность","lesson_short_name":"Социальная стратификация","lesson_chapter":"Социология","lesson_img_adr":"\/images\/lessons\/29.png","status":"2"},
    {"lesson_number":"28","lesson_name":"Практика по разделу \"Экономика\"","lesson_short_name":"Практика по экономике","lesson_chapter":"Экономика","lesson_img_adr":"\/images\/lessons\/28.png","status":"2"},
    {"lesson_number":"27","lesson_name":"Социальный статус и роль. Социальный конфликт.","lesson_short_name":"Социальный статус и роль","lesson_chapter":"Социология","lesson_img_adr":"\/images\/lessons\/27.png","status":"2"},
    {"lesson_number":"26","lesson_name":"Социальная группа. Молодежь как социальная группа.","lesson_short_name":"Социальная группа","lesson_chapter":"Социология","lesson_img_adr":"\/images\/lessons\/26.png","status":"2"},
    {"lesson_number":"25","lesson_name":"Мировая экономика. Рациональное экономическое поведение.","lesson_short_name":"Мировая экономика","lesson_chapter":"Экономика","lesson_img_adr":"\/images\/lessons\/25.png","status":"2"},
    {"lesson_number":"24","lesson_name":"Экономика","lesson_short_name":"Экономика","lesson_chapter":"Экономика","lesson_img_adr":"\/images\/lessons\/24.png","status":"2"},
    {"lesson_number":"23","lesson_name":"Практика по экономике (часть 1)","lesson_short_name":"Практика по экономике","lesson_chapter":"Экономика","lesson_img_adr":"\/images\/lessons\/23.png","status":"2"},
    {"lesson_number":"22","lesson_name":"Финансовые институты.Банковская система.","lesson_short_name":"Финансовые институты","lesson_chapter":"Экономика","lesson_img_adr":"\/images\/lessons\/22.png","status":"2"},
    {"lesson_number":"21","lesson_name":"Фирма в рыночной экономике","lesson_short_name":"Фирма в рыночной экономике","lesson_chapter":"Экономика","lesson_img_adr":"\/images\/lessons\/21.png","status":"2"},
    {"lesson_number":"20","lesson_name":"Рынок и рыночный механизм. Спрос и предложение","lesson_short_name":"Рынок","lesson_chapter":"Экономика","lesson_img_adr":"\/images\/lessons\/20.png","status":"2"},
    {"lesson_number":"19","lesson_name":"Ценные бумаги","lesson_short_name":"Ценные бумаги","lesson_chapter":"Экономика","lesson_img_adr":"\/images\/lessons\/19.png","status":"2"},
    {"lesson_number":"18","lesson_name":"Организационно-правовые формы предпринимательской деятельности.","lesson_short_name":"Формы предпринимательской деятельности.","lesson_chapter":"Экономика","lesson_img_adr":"\/images\/lessons\/18.png","status":"2"},
    {"lesson_number":"17","lesson_name":"Практика по разделу \"Экономика\" (часть 1)","lesson_short_name":"Практика по экономике","lesson_chapter":"Экономика","lesson_img_adr":"\/images\/lessons\/17.png","status":"2"},
    {"lesson_number":"16","lesson_name":"Финансовые институты.Банковская система.","lesson_short_name":"Финансовые институты","lesson_chapter":"Экономика","lesson_img_adr":"\/images\/lessons\/16.png","status":"2"},
    {"lesson_number":"15","lesson_name":"Фирма в рыночной экономике","lesson_short_name":"Фирма в рыночной экономике","lesson_chapter":"Экономика","lesson_img_adr":"\/images\/lessons\/15.png","status":"2"},
    {"lesson_number":"14","lesson_name":"Рынок и рыночный механизм. Спрос и предложение","lesson_short_name":"Рынок","lesson_chapter":"Экономика","lesson_img_adr":"\/images\/lessons\/14.png","status":"2"},
    {"lesson_number":"13","lesson_name":"Типы экономических систем","lesson_short_name":"Экономические системы","lesson_chapter":"Экономика","lesson_img_adr":"\/images\/lessons\/13.png","status":"2"},
    {"lesson_number":"12","lesson_name":"Экономика и экономическая теория.Факторы производства и факторный доход.","lesson_short_name":"Экономика и экономическая теория","lesson_chapter":"Экономика","lesson_img_adr":"\/images\/lessons\/12.png","status":"2"},
    {"lesson_number":"11","lesson_name":"Повторение по разделу \"Человек и общество\"","lesson_short_name":"Повторение ЧиО","lesson_chapter":"Человек и общество","lesson_img_adr":"\/images\/lessons\/11.png","status":"2"},
    {"lesson_number":"10","lesson_name":"Понятие общественного прогресса.Многовариантность общественного развития.Типы общества.Глобализация.","lesson_short_name":"Общественный прогресс","lesson_chapter":"Человек и общество","lesson_img_adr":"\/images\/lessons\/10.png","status":"4"},
    {"lesson_number":"9","lesson_name":"Религия,искусство и мораль","lesson_short_name":"Религия,искусство и мораль","lesson_chapter":"Человек и общество","lesson_img_adr":"\/images\/lessons\/9.png","status":"2"},
    {"lesson_number":"8","lesson_name":"Наука и образование.","lesson_short_name":"Наука и образование.","lesson_chapter":"Человек и общество","lesson_img_adr":"\/images\/lessons\/8.png","status":"2"},
    {"lesson_number":"7","lesson_name":"Понятие культуры.Формы и разновидности культуры.","lesson_short_name":"Понятие культуры","lesson_chapter":"Человек и общество","lesson_img_adr":"\/images\/lessons\/7.png","status":"2"},
    {"lesson_number":"6","lesson_name":"Общество как система.Основные институты общества.","lesson_short_name":"Общество как система","lesson_chapter":"Человек и общество","lesson_img_adr":"\/images\/lessons\/6.png","status":"2"},
    {"lesson_number":"5","lesson_name":"Сознание и познание.","lesson_short_name":"Сознание и познание.","lesson_chapter":"Человек и общество","lesson_img_adr":"\/images\/lessons\/5.png","status":"2"},
    {"lesson_number":"4","lesson_name":"Мировоззрение,его виды и формы.","lesson_short_name":"Мировоззрение","lesson_chapter":"Человек и общество","lesson_img_adr":"\/images\/lessons\/4.png","status":"2"},
    {"lesson_number":"3","lesson_name":"Человек,индивид и личность.","lesson_short_name":"Человек,индивид и личность","lesson_chapter":"Человек и общество","lesson_img_adr":"\/images\/lessons\/3.png","status":"2"},
    {"lesson_number":"2","lesson_name":"Природное и общественное в человеке. Социализация.","lesson_short_name":"Социализация","lesson_chapter":"Человек и общество","lesson_img_adr":"\/images\/lessons\/2.png","status":"2"},
    {"lesson_number":"1","lesson_name":"Общество как часть материального мира.","lesson_short_name":"Общество","lesson_chapter":"Человек и общество","lesson_img_adr":"\/images\/lessons\/1.png","status":"2"},
    {"lesson_number":"0","lesson_name":"Общее","lesson_short_name":"Общее","lesson_chapter":"Курс по психологии","lesson_img_adr":"\/images\/lessons\/0.png","status":"3"}
]
"""

    var lessonsRoadList: ArrayList<Map<String, String>>? = null
    var lessonsRoadListStatus =
        MutableLiveData<LessonsRoadListStatus>(LessonsRoadListStatus.SUCCESS)

    var errMess = ""

    var elementsAfterFirstUnfulfilledLessonHeightSum = 0

    var firstUnfulfilledLesson: Map<String, String>? = mapOf()

    // Переменная которая содержит сгруппированные по разделам уроки
    var groupedLessons = arrayListOf<ArrayList<Map<String, String>>>()

    // Высота RecyclerView, до которого нужно сделать скролл
    var toScrollRvHeight = 0

    // Индекс списка RecyclerView, до которого нужно сделать скролл
    var scrollToIndex = 0

    // Урок, до которого нужно сделать скролл
    var scrollToLesson = mapOf<String, String>()

    // Список, который содержит урок, до которого нужно сделать скролл
    var scrollToLessons = ArrayList<Map<String, String>>()

    // Сумма высоты элементов RecyclerView до элемента, до которой нужно долистать
    var scrollRecyclerHeightSumAfterScrollElement = 0

    // Y до которой нужно сделать скролл
    var scrollY = 0

    // Переменная, которая указывет первое ли это открытие фрагмента
    var isFirstOpen = true

    // Инициализация вьюмодели
    init {
                setLessonsRoadList()
    }

    // Получение списка координат от стартовой точки до конечной
    private fun getCoordinates(startPoint: Point, endPoint: Point): Map<Double, Double> {
        val startX: Double = startPoint.x.toDouble()
        val startY: Double = startPoint.y.toDouble()

        var calculatedCoordinates: Map<Double, Double> = mutableMapOf()
        val lineLength = sqrt(
            (endPoint.x - startPoint.x).toDouble().pow(2) +
                    (endPoint.y - startPoint.y).toDouble().pow(2)
        )

        val howMuchElems: Int = (lineLength / 26).toInt()

        val angleA = (endPoint.x - startPoint.x) / lineLength
        val angleB = (endPoint.y - startPoint.y) / lineLength
        for (i in 0..howMuchElems) {
            val point = Point(
                (startX + (i * 26).toDouble() * angleA).toInt(),
                (startY + (i * 26).toDouble() * angleB).toInt()
            )
            calculatedCoordinates += point.x.toDouble() to point.y.toDouble()
        }
        return calculatedCoordinates
    }

    // Получение списка разделов с уроками
    fun getLessonsByChapter(lessons: ArrayList<Map<String, String>>): ArrayList<ArrayList<Map<String, String>>> {
        val groupedLessons = arrayListOf<ArrayList<Map<String, String>>>()
        var group = arrayListOf<Map<String, String>>()
        group.add(lessons[0])
        for (i in lessons.indices) {
            if (i < lessons.lastIndex) {
                if (lessons[i]["lesson_chapter"] == lessons[i + 1]["lesson_chapter"]) {
                    group.add(lessons[i + 1])
                } else {
                    groupedLessons.add(group)
                    group = arrayListOf()
                    group.add(lessons[i + 1])
                }
            }

            if (i == lessons.lastIndex) {
                groupedLessons.add(group)
            }
        }

        return groupedLessons
    }

    // Установка доржки уроков
    private fun setLessonsRoadList() {
        val dataArray: ArrayList<Map<String,String>> = Gson().fromJson(jsonStringData, object:
            TypeToken<ArrayList<Map<String, String>>>(){}.type)
                lessonsRoadList = dataArray
                firstUnfulfilledLesson = findFirstUnfulfilledLessonPosition(dataArray)
    }

    // Функция для получения первого невыполненного урокаfun
    fun findFirstUnfulfilledLessonPosition(lessons: ArrayList<Map<String, String>>): Map<String, String> {
        val statusOne = lessons.findLast { it["status"] == "1" } ?: lessons[0]
        val statusTwo = lessons.findLast { it["status"] == "2" } ?: lessons[0]
        val statusOneNumber = statusOne["lesson_number"]
        val statusTwoNumber = statusTwo["lesson_number"]
        return if (statusOneNumber!!.toInt() > statusTwoNumber!!.toInt()) {
            statusTwo
        } else {
            statusOne
        }
    }

    // Функция для получения цвета для плашки раздела
    fun getColorForChapterCardView(lessonChapter: String): Int {
        return when (lessonChapter) {
            "Политика" -> ContextCompat.getColor(
                app.applicationContext,
                R.color.politic_chapter_cardview_color
            )
            "Социология" -> ContextCompat.getColor(
                app.applicationContext,
                R.color.society_lesson_chapter_cardview_color
            )
            "Экономика" -> ContextCompat.getColor(
                app.applicationContext,
                R.color.economic_chapter_cardview_color
            )
            "Человек и общество" -> ContextCompat.getColor(
                app.applicationContext,
                R.color.humanity_lesson_chapter_cardview_color
            )
            "Право" -> ContextCompat.getColor(
                app.applicationContext,
                R.color.human_rights_lesson_chapter_cardview_color
            )
            else -> ContextCompat.getColor(
                app.applicationContext,
                R.color.default_lesson_chapter_cardview_color
            )
        }
    }

    // Получение позиции раздела в списке по номеру урока
    fun getChapterPositionByLessonNumber(
        lessonNumber: String,
        chapters: ArrayList<ArrayList<Map<String, String>>>
    ): Int {
        for (i in chapters.indices) {
            for (lesson in chapters[i]) {
                if (lesson["lesson_number"] == lessonNumber) {
                    return i
                }
            }
        }
        return 0
    }

    // Функция для получения названия статуса по номеру статуса
    fun setLessonStatusNameById(id: String): String {
        return when (id) {
            "0" -> "Недоступно"
            "1" -> "Не начато"
            "2" -> "Выполняется"
            "3" -> "Сделаны базовые"
            "4" -> "Сделано всё"
            else -> ""
        }
    }

    // Функция для получения цвета по номеру статуса
    fun getLessonsStatusColorById(id: String): Int {
        return Color.parseColor(
            when (id) {
                "0" -> "#808080"
                "1" -> "#E93B3B"
                "2" -> "#FFB800"
                "3" -> "#27A4FF"
                "4" -> "#1DCD00"
                else -> ""
            }
        )
    }

    // Функция для рисования линии левым и правым кружком
    fun createLineBitmapLeftToRight(
        path: Path,
        paint: Paint,
        width: Int,
        height: Int
    ): Bitmap {
        val bitmap =
            Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )
        val canvas = Canvas(bitmap)
        val coordinates: Map<Double, Double> =
            getCoordinates(Point(15, 15), Point(width - 15, height - 15))
        var i = 0
        for ((key, value) in coordinates) {
            if (i != coordinates.size - 2) {
                if (i % 2 == 0) {
                    path.addCircle(key.toFloat(), value.toFloat(), 12f, Path.Direction.CW)
                } else {
                    if (i != coordinates.size - 1) {
                        path.addCircle(key.toFloat(), value.toFloat(), 6f, Path.Direction.CW)
                    }
                }
                i++
            }
        }
        canvas.drawPath(path, paint)
        return bitmap
    }

    // Функция для рисования линии правым и левым кружком
    fun createLineBitmapRightToLeft(
        path: Path,
        paint: Paint,
        width: Int,
        height: Int
    ): Bitmap {
        val bitmap =
            Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )
        val canvas = Canvas(bitmap)
        val coordinates: Map<Double, Double> =
            getCoordinates(Point(15, 15), Point(width - 15, height - 15))
        var i = 0
        for ((key, value) in coordinates) {
            if (i % 2 == 0) {
                path.addCircle(key.toFloat(), value.toFloat(), 12f, Path.Direction.CW)
            } else {
                if (i != coordinates.size - 1) {
                    path.addCircle(key.toFloat(), value.toFloat(), 6f, Path.Direction.CW)
                }
            }
            i++
        }
        canvas.drawPath(path, paint)
        return bitmap
    }

    // Функция для рисования линии между разделами
    fun createLineBetweenChapters(
        path: Path,
        paint: Paint,
        width: Int,
        height: Int
    ): Bitmap {
        val bitmap =
            Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )
        val canvas = Canvas(bitmap)
        val coordinates: Map<Double, Double> =
            getCoordinates(Point(12, 12), Point(width - 15, height - 15))
        var i = 0
        for ((key, value) in coordinates) {
            if (i % 2 == 0) {
                path.addCircle(key.toFloat(), value.toFloat(), 12f, Path.Direction.CW)
            } else {
                path.addCircle(key.toFloat(), value.toFloat(), 6f, Path.Direction.CW)
            }
            i++
        }
        canvas.drawPath(path, paint)
        return bitmap
    }

    // Получение сгруппированного списка по разделам
    fun getGroupedLessonsWithViewType(groupedLessons: ArrayList<ArrayList<Map<String, String>>>): ArrayList<GroupedLessonsWithViewType> {
        val groupedLessonsWithViewTypeList =
            arrayListOf<GroupedLessonsWithViewType>()
        groupedLessons.reversed().forEachIndexed { index, lessons ->
            val groupedLessonsWithViewType: GroupedLessonsWithViewType
            if (index == 0) {
                groupedLessonsWithViewType = if (lessons.lastIndex % 2 == 0) {
                    GroupedLessonsWithViewType(LessonsRoadAdapter.VIEW_TYPE_LEFT, lessons)
                } else {
                    GroupedLessonsWithViewType(LessonsRoadAdapter.VIEW_TYPE_RIGHT, lessons)
                }
            } else {
                groupedLessonsWithViewType =
                    if (groupedLessonsWithViewTypeList[index - 1].viewType == LessonsRoadAdapter.VIEW_TYPE_RIGHT) {
                        if (lessons.lastIndex % 2 == 0) {
                            GroupedLessonsWithViewType(LessonsRoadAdapter.VIEW_TYPE_LEFT, lessons)
                        } else {
                            GroupedLessonsWithViewType(LessonsRoadAdapter.VIEW_TYPE_RIGHT, lessons)
                        }
                    } else {
                        if (lessons.lastIndex % 2 == 0) {
                            GroupedLessonsWithViewType(LessonsRoadAdapter.VIEW_TYPE_RIGHT, lessons)
                        } else {
                            GroupedLessonsWithViewType(LessonsRoadAdapter.VIEW_TYPE_LEFT, lessons)
                        }
                    }
            }
            groupedLessonsWithViewTypeList.add(groupedLessonsWithViewType)
        }
        return ArrayList(groupedLessonsWithViewTypeList.reversed())
    }

    // Получения цвета раздела по названию
    fun getBackgroundColorForChapter(chapter: String): Int {
        return when (chapter) {
            "Политика" -> ContextCompat.getColor(app, R.color.politic_lesson_chapter_color)
            "Социология" -> ContextCompat.getColor(app, R.color.society_lesson_chapter_color)
            "Экономика" -> ContextCompat.getColor(app, R.color.economic_lesson_chapter_color)
            "Человек и общество" -> ContextCompat.getColor(app, R.color.humanity_lesson_chapter_color)
            "Право" -> ContextCompat.getColor(app, R.color.human_rights_lesson_chapter_color)
            else -> ContextCompat.getColor(app, R.color.default_chapter_color)
        }
    }

    // Получения цвета линий раздела по названию
    fun getLineColorForChapter(chapter: String): Int {
        return when (chapter) {
            "Политика" -> ContextCompat.getColor(app, R.color.politic_line_color)
            "Социология" -> ContextCompat.getColor(app, R.color.society_line_color)
            "Экономика" -> ContextCompat.getColor(app, R.color.economic_line_color)
            "Человек и общество" -> ContextCompat.getColor(app, R.color.humanity_line_color)
            "Право" -> ContextCompat.getColor(app, R.color.human_rights_line_color)
            else -> ContextCompat.getColor(app, R.color.default_line_color)
        }
    }

    // Получения цвета для картинки параллакса по названию раздела
    fun getParallaxImageColorForChapter(chapter: String): Int {
        return when (chapter) {
            "Политика" -> ContextCompat.getColor(app, R.color.politic_line_color)
            "Социология" -> ContextCompat.getColor(app, R.color.society_line_color)
            "Экономика" -> ContextCompat.getColor(app, R.color.economic_line_color)
            "Человек и общество" -> ContextCompat.getColor(app, R.color.humanity_line_color)
            "Право" -> ContextCompat.getColor(app, R.color.human_rights_line_color)
            else -> ContextCompat.getColor(app, R.color.default_line_color)
        }
    }

}

// Статус дорожки урока
enum class LessonsRoadListStatus {
    LOADING, SUCCESS, UNLOG, ERROR
}