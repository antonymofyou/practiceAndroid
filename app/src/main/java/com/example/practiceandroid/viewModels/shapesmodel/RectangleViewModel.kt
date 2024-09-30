package com.example.practiceandroid.viewModels.shapesmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.practiceandroid.models.ResponseShapes
import com.example.practiceandroid.views.contextmenu.color.getHexColorFromRGB
import kotlin.math.roundToInt

class RectangleViewModel(shape: ResponseShapes.Shape): ViewModel() {
    // Границы элемента
    var top = mutableFloatStateOf(0f)
    var left = mutableFloatStateOf(0f)
    var right = mutableFloatStateOf(0f)
    var bottom = mutableFloatStateOf(0f)

    var isInitialUserSize = mutableStateOf(false)

    // Глобальный оффсет элемента
    var rectangleOffsetInWindow = mutableStateOf(Offset.Zero)

    var showContextMenu = mutableStateOf(false)// Контекстное меню
    var showDeleteDialog = mutableStateOf(false)// Окно подтверждения удаления
    var showResizeDialog = mutableStateOf(false)// Окно изменения размеров
    var showChangeBackgroundColorDialog = mutableStateOf(false)
    var showChangeBorderColorDialog = mutableStateOf(false)
    var showChangeBorderSettingDialog = mutableStateOf(false)

    var width: MutableState<Dp> = mutableStateOf(0.dp)
    var height: MutableState<Dp> = mutableStateOf(0.dp)

    var maxW: MutableState<Dp> = mutableStateOf(0.dp)
    var maxH: MutableState<Dp> = mutableStateOf(0.dp)
    var minW: MutableState<Dp> = mutableStateOf(0.dp)
    var minH: MutableState<Dp> = mutableStateOf(0.dp)

    var zIndex: MutableState<Float> = mutableStateOf(0f)

    var offset: MutableState<Offset> = mutableStateOf(Offset.Zero)
    var rotation: MutableState<Float> = mutableStateOf(0f)

    var cornerRadius: MutableState<Dp> = mutableStateOf(0.dp)
    var borderWidth: MutableState<Dp> = mutableStateOf(0.dp)
    var color: MutableState<String> = mutableStateOf("")
    var borderColor: MutableState<String> = mutableStateOf("")

    var textVerticalAlignment: String?
    var text: List<ResponseShapes.Shape.Text>?

    var visibility = mutableStateOf(true)

    // Отслеживание позиции нажатия
    val touchOffset = mutableStateOf(Offset.Zero)

    init {
        width.value = shape.width.dp
        height.value = shape.height.dp

        maxW.value = (shape.width.dp * 3).value.roundToInt().dp
        minW.value = (shape.width.dp * 0.85f).value.roundToInt().dp
        maxH.value = (shape.height.dp * 3).value.roundToInt().dp
        minH.value = (shape.height.dp * 0.85f).value.roundToInt().dp

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

class RectangleViewModelFactory(private val shape: ResponseShapes.Shape) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RectangleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RectangleViewModel(shape) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}