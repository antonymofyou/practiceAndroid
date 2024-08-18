package com.example.practiceandroid.viewModels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.example.practiceandroid.models.ResponseShapes
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class MainViewModel(app: Application): AndroidViewModel(app) {

    //Как будто ответ пришел от сервера, пока так оставлю
    val json = """{
  "shapes": [
    {
      "id": 1,
      "type": "rectangle",
      "x": 170,
      "y": 100,
      "width": 200,
      "height": 100,
      "color": "#FF5733",
      "borderColor": "#C70039",
      "zIndex": 0.5,
      "cornerRadius": 5,
      "borderWidth": 5,
      "rotation": 90
    },
    {
      "id": 2,
      "type": "image",
      "x": 200,
      "y": 200,
      "width": 150,
      "height": 150,
      "imageId": "1",
      "zIndex": 0.7,
      "cornerRadius": 40
    },
    {
      "id": 6,
      "type": "circle",
      "x": 170,
      "y": 100,
      "width": 200,
      "height": 100,
      "color": "#9ddb6b",
      "borderColor": "#C70039",
      "zIndex": 0.5,
      "cornerRadius": 20,
      "borderWidth": 5
    },
    {
      "id": 3,
      "type": "arrow",
      "x": 100,
      "y": 100,
      "width": 200,
      "height": 200,
      "fill": "#C70039",
      "borderColor": "#C70039",
      "zIndex": 2,
      "startRotation": 110
    },
    {
      "id": 4,
      "type": "rectangle",
      "x": 300,
      "y": 300,
      "width": 200,
      "height": 100,
      "color": "#FF5733",
      "borderColor": "#C70039",
      "zIndex": 0.5,
      "textVerticalAlignment": "top",
      "text": [
        {
          "alignment": "left",
          "text": [
            {
              "text": "Hello",
              "type": "bold",
              "fontSize": 24,
              "fontColor": "#333333",
              "textDecoration": "underline"
            },
            {
              "text": " This is a test text.",
              "fontSize": 18,
              "fontColor": "#333333"
            }
          ]
        },
        {
          "alignment": "right",
          "text": [
            {
              "text": "Hello",
              "type": "bold",
              "fontSize": 24,
              "fontColor": "#333333"
            },
            {
              "text": " This is a test text.",
              "fontSize": 18,
              "fontColor": "#333333"
            }
          ]
        }
      ],
      "borderWidth": 5
    },
    {
      "id": 30,
      "type": "rectangle",
      "x": 230,
      "y": 177,
      "width": 200,
      "height": 100,
      "color": "#FF5733",
      "borderColor": "#C70039",
      "zIndex": 1,
      "textVerticalAlignment": "top",
      "borderWidth": 5
    }
  ],
  "imageDictionary": {
    "1": "exampleImageBase64"
  }
}
""".trimIndent()
    //Переводим ответ от Json в data class
    var responseShapes = mutableStateOf(Gson().fromJson(json, ResponseShapes::class.java))
}