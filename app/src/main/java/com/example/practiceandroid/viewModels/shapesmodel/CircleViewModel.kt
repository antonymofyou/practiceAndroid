package com.example.practiceandroid.viewModels.shapesmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.practiceandroid.models.ResponseShapes
import com.example.practiceandroid.views.contextmenu.color.getHexColorFromRGB

class CircleViewModel(shape: ResponseShapes.Shape): ViewModel() {

    var showContextMenu = mutableStateOf(false)// Контекстное меню
    var showDeleteDialog = mutableStateOf(false)// Окно подтверждения удаления
    var showResizeDialog = mutableStateOf(false)// Окно изменения размеров
    var showChangeBackgroundColorDialog = mutableStateOf(false)
    var showChangeBorderColorDialog = mutableStateOf(false)
    var showChangeBorderSettingDialog = mutableStateOf(false)

    var width: MutableState<Dp> = mutableStateOf(0.dp)
    var height: MutableState<Dp> = mutableStateOf(0.dp)
    var zIndex: MutableState<Float> = mutableStateOf(0f)

    var offset: MutableState<Offset> = mutableStateOf(Offset.Zero)
    var scaleX: MutableState<Float> = mutableStateOf(1f)
    var scaleY: MutableState<Float> = mutableStateOf(1f)
    var rotation: MutableState<Float> = mutableStateOf(0f)

    var cornerRadius: MutableState<Dp> = mutableStateOf(0.dp)
    var borderWidth: MutableState<Dp> = mutableStateOf(0.dp)
    var color: MutableState<String> = mutableStateOf("")
    var borderColor: MutableState<String> = mutableStateOf("")

    var textVerticalAlignment: String?
    var text: List<ResponseShapes.Shape.Text>?

    var visibility = mutableStateOf(true)

    init {
        width.value = shape.width.dp
        height.value = shape.height.dp
        zIndex.value = shape.zIndex

        offset.value = Offset(shape.x, shape.y)
        rotation.value = shape.rotation ?: 0f

        color.value = shape.color ?: getHexColorFromRGB(Color.Black.toArgb())
        borderColor.value = shape.borderColor ?: getHexColorFromRGB(Color.Black.toArgb())
        cornerRadius.value = shape.cornerRadius?.dp ?: 0.dp
        borderWidth.value = shape.borderWidth?.dp ?: 0.dp

        textVerticalAlignment = shape.textVerticalAlignment

        text = shape.text
    }

    fun deleteShape(){
        visibility.value = false
    }
}

class CircleViewModelFactory(private val shape: ResponseShapes.Shape) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CircleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CircleViewModel(shape) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}