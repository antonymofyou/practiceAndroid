package com.example.practiceandroid

object ConfigData {
    const val BASE_URL = "https://razrab.nasotku.ru"
    const val APP_USER_AGENT = "nasotku-android-app"
    const val LANDING_LINK = BASE_URL + "/kurs"

    //Токен при первичной авторизации на сервере
    const val FIRST_TOKEN = "sdln23ojfsLKJF#sd#lsfdnLKF"

    //Переменные для формирования подписи запроса
    const val APP_SECRET_KEY_INT = 9482632914807324
    const val APP_SECRET_KEY_STRING = "FLLKL#9HFLANDNLLA*D&FLKSAD"

    //Баллы за задания
    val TASK_BALLS = arrayOf(0, 1, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 1, 2, 2, 2, 2,
        2, 2, 3, 3, 3, 4, 3, 4, 6, 0, 0, 0, 0, 2)

    // Имя директории где хранятся файлы WebView
    val DIR_NAME = "jsAndcss"
    val dirToTaskP1Images = "questions"
    val dirToTaskP2Images = "questions2"

    val pathToQuestions2 = "/questions2/"
    val pathToQuestions = "/questions/"

    // Имя файла .css стилей
    val CSS_FILE_NAME = "style_ht2_arrows.css"

    // Имена файлов .js скриптов
    val ARROW_JS_NAME = "arrows.js"
    val JQUERY_MIN_JS_NAME = "jquery.min.js"

    // Интервал для синхронизации в игре "Реши первым"
    val GAME_DECIDE_FIRST_TIMER_SYNC_INTERVAL = 3000L

    // Интервал для синхронизации в видеочате
    val TRAIN_VERBAL_ZACHETS_CHAT_SYNC_INTERVAL = 2000L
}
