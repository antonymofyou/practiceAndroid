package ru.nasotku.nasotkuapp.api.rootclasses

import com.example.practiceandroid.ConfigData
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import kotlin.reflect.full.memberProperties


//базовый клас, который делает подпись и содержит signature
//от этого класса уже наследуются другие классы API
//@Keep //Позволяет не менять данный класс ProGuard
open class API_root_class() {
    var signature:String = ""//подпись, которая формируется с помощью метода make_signature() из этого класса
    fun calc_signature(nasotku_token:String):String{//метод вычисляет подпись. Конкатенируем все поля, прибавляем в конце ключи. Затем шифруем sha256.
        val fields=this::class.memberProperties.sortedBy { it.name }
        var field_value=""
        var signature_text="" //подпись

        //Конкатенируем все переменные класса
        for(field in fields){
            field_value=field.getter.call(this).toString()
            if(field.name!="signature" && field.getter.call(this) is String) signature_text+=field_value //конкатенируем все значения в подписи
            //В подпись не добавляем signature и типы List
        }

        //добавляем(конкатенируем) два ключа(int+string) и добавляем токен
        val app_secret_key_int = ConfigData.APP_SECRET_KEY_INT
        val app_secret_key_str = ConfigData.APP_SECRET_KEY_STRING
        signature_text+=app_secret_key_int.toString() + app_secret_key_str + nasotku_token

        return Hex.encodeHex(DigestUtils.sha256(signature_text)).concatToString()//DigestUtils.sha256Hex(signature_text).toString()
    }

    fun make_signature(nasotku_token: String){//метод назначает подпись данному объекту
        this.signature=calc_signature(nasotku_token)
    }

    fun check_signature(nasotku_token: String):Boolean{//проверка подписи для входящих запросов(полученных ответов)
        return this.signature==calc_signature(nasotku_token) //возвращает единицу, если подпись, которая пришла совпадает с вычисленной
    }
}