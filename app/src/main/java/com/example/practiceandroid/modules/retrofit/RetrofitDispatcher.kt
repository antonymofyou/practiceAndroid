package com.example.practiceandroid.modules.retrofit

import android.content.Context
import com.example.practiceandroid.ConfigData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream

object RetrofitDispatcher {
    val first_token = ConfigData.FIRST_TOKEN

    val iface = Retrofit.Builder()
        .baseUrl(ConfigData.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(interfaceAPI::class.java)

    //метод скачивания файла. addUrl - доп адрес URL к основному серверу. fname - имя файла для записи. dir - папка для записи файла
    fun downloadFile(
        context: Context,
        addUrl: String,
        dir: String,
        fname: String,
        lambdaDownloadFile: (success: Boolean, mess: String?) -> Unit
    ) {
        try {
            val result = iface.download(addUrl).execute()

            if (result.isSuccessful == false) {
                lambdaDownloadFile(false, "Неуспешное скачивание ($fname)")
                return
            }
            if (result.body() == null) {
                lambdaDownloadFile(false, "Нулевой ответ сервера ($fname)")
                return
            }

            val bytes = result.body()!!.bytes()

            //Папка файла
            val fileDir = context.getFilesDir().toString() + "/" + dir

            //Если директория не существует - создаем
            if (!File(fileDir).exists()) {
                File(fileDir).mkdir()
            }

            val filename = fileDir + "/" + fname
            val file = File(filename)

            //Если файл не существует - создаём
            if (!file.exists()) {
                file.createNewFile()
            }

            //создаем поток записи в файл
            val stream = FileOutputStream(file)
            stream.write(bytes)

            //Log.d("MyLog","Скачан файл "+filename)

            lambdaDownloadFile(true, null)
        } catch (e: Exception) {
            lambdaDownloadFile(false, "Нет подключения к серверу? " + e.message)
        }
    }

}