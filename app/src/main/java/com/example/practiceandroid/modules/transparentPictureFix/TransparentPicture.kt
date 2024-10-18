package com.example.practiceandroid.modules.transparentPictureFix

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.practiceandroid.R

/*
Нужно сделать обработку нажатия на часть картинки,
чтобы если она прозрачная, то клик регистрировался на задние элементы.

Для теста сделай пару кнопок в разных частях экрана и
поверх выводи картинку, чтобы какая то ее часть была прозрачная
и при клике на кнопку выводи лог какой нибудь.
Получается поверх всех элементов будет картинка с прозрачными участками
и там кнопка, нужно чтобы при нажатии на кнопку выводился лог

Я думаю, нужно будет картинку как ImageBitmap загружать и проверять
по нажатию на часть картинки являются ли цвет пикселей прозрачным на этом участке.
Но если свое решение сделаешь - без проблем.
 */

@Composable
fun TransparentPicture(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            TestButton(text = "button")
            Spacer(modifier = Modifier.height(16.dp))
            TestButton(text = "button")
            Spacer(modifier = Modifier.height(16.dp))
            TestButton(text = "button")
            Spacer(modifier = Modifier.height(16.dp))
            TestButton(text = "button")
            Spacer(modifier = Modifier.height(16.dp))
            TestButton(text = "button")
            Spacer(modifier = Modifier.height(16.dp))
            TestButton(text = "button")
            Spacer(modifier = Modifier.height(16.dp))
            TestButton(text = "button")
            Spacer(modifier = Modifier.height(16.dp))
            TestButton(text = "button")
            Spacer(modifier = Modifier.height(16.dp))
            TestButton(text = "button")
        }
        Image(
            painter = painterResource(id = R.drawable.transparent_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun TestButton(
    text: String
){
    Button(
        onClick = { Log.d("TestButton", "click") },
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = text
        )
    }
}