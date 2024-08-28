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
      "width": 100,
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
      "x": 0,
      "y": 300,
      "width": 400,
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
              "text": " Test text.",
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
              "text": " Test text.",
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
    "1": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAIAAAD/gAIDAAAAAXNSR0IArs4c6QAAAoNJREFUeJzt202Ko0AAhmEd5gxZB9wHIeQKyQVCeRE9RbnOISQX0EsoWQqNuUlmUUMhdn78JHZseJ+VbbUpfUlMNbRBAODDQr91u90+eibLFYb/K/359Jn8JsQSEEtALAGxBMQSEEtALAGxBMQSEEtALAGxBMQSEEtALAGxBMQSEEtALAGxBMQSEEtALAGxBMQSEEtALAGxBMQSEEtALAGxBMQSEEtALAGxBMQSEEswMVZVVWEY+n8Q95qmSZLEDSVJ0jTN+NElzzt0G63rurtH9fd7XdeNGV3yvNNjWWsfHWWMcTuttf7XjDFjRhc77/RYfuK7R/lzGlzey9G6rt2VlGXpRsuydHvqup5vXjXW3zvZXnHzZVnW33m9Xt3GZrMZbPihR6NxHB+Px6+vL/8I0eFwCIIgiqI0Teebd71eSxeu3eB3u11d1/0L8Nq2fXRU27bPR4MgOJ1O7sc8z5Mkcdvn83nueSXaO+vu6b7Ffr+31mZZ5t841to4jueeV7KgdVaaplEUuW1jzEIC9U25Z921Wq0mDA1Gt9utu3P98Lwjve2d5T8yl8tlsBHH8fNRt5HneVEUbrsoijzPf2beKcZ/lT76AnZf8FEUdV3XdZ37TA3WO49G67p2L2iM8QsFt26YdV5p6fDOWGVZfn/Z/tLpyai/W/XX3FEUzT3vx2J9X2f7peDzUb//yZ455h3JHzjLA+VVVbnVwITRBc7r/27n6fvXePp+CmIJiCUgloBYAmIJiCUgloBYAmIJiCUgloBYAmIJiCUgloBYAmIJiCUgloBYAmIJiCUgloBYAmIJiCUgloBYAmIJiCUgloBYAmIJiCUgloBYAmIJiAXgN/kHCNX2Mu0yEwMAAAAASUVORK5CYII="
  }
}
""".trimIndent()
    //Переводим ответ от Json в data class
    var responseShapes = mutableStateOf(Gson().fromJson(json, ResponseShapes::class.java))
}