package com.example.practiceandroid.modules.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface interfaceAPI {
    /**Скачивание файлы по ссылке*/
    @GET
    fun download(@Url url:String): Call<ResponseBody>
}

