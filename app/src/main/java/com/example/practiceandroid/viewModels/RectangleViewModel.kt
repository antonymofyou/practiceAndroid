package com.example.practiceandroid.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.practiceandroid.models.ResponseShapes

class RectangleViewModel(shape: ResponseShapes.Shape): ViewModel() {

    var showContextMenu = mutableStateOf(false)// Контекстное меню
    var showDeleteDialog = mutableStateOf(false)// Окно подтверждения удаления
    var showResizeDialog = mutableStateOf(false)// Окно изменения размеров

    var width: MutableState<Dp> = mutableStateOf(0.dp)
    var height: MutableState<Dp> = mutableStateOf(0.dp)
    var zIndex: MutableState<Float> = mutableStateOf(0f)

    var offset: MutableState<Offset> = mutableStateOf(Offset.Zero)
    var scale: MutableState<Float> = mutableStateOf(1f)
    var rotation: MutableState<Float> = mutableStateOf(0f)

    var cornerRadius: Dp
    var borderWidth: Dp
    var color: String?
    var borderColor: String?

    var textVerticalAlignment: String?
    var text: List<ResponseShapes.Shape.Text>?

    var visibility = mutableStateOf(true)

    init {
        width.value = shape.width.dp
        height.value = shape.height.dp
        zIndex.value = shape.zIndex

        offset.value = Offset(shape.x, shape.y)
        rotation.value = shape.rotation ?: 0f

        color = shape.color
        borderColor = shape.borderColor
        cornerRadius = shape.cornerRadius?.dp ?: 0.dp
        borderWidth = shape.borderWidth?.dp ?: 0.dp

        textVerticalAlignment = shape.textVerticalAlignment

        text = shape.text
    }

    fun updateShape(width: Dp, height: Dp, zIndex: Float){
        this.width.value = width
        this.height.value = height
        this.zIndex.value = zIndex
    }

    fun deleteShape(){
        visibility.value = false
    }
}

class RectangleViewModelFactory(private val shape: ResponseShapes.Shape) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RectangleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RectangleViewModel(shape) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}