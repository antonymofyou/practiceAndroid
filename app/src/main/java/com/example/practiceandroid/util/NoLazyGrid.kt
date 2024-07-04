package ru.nasotku.nasotkuapp.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

// Элемент для прогрузки списка в сетку без использования Lazy

@Composable
fun NoLazyGrid(
    // Ширина одного элемента
    itemWidth: Int,
    // Количество элементов
    itemCount: Int,
    // Горизонтальный отступ между элементами
    spaceHorizontal: Int,
    // Вертикальный отступ между элементами
    spaceVertical: Int,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {
    // Ширина column, инициализируем по-умолчанию 0.dp
    var columnWidth by remember { mutableStateOf(0.dp) }
    // Получаем текущее значение плотности пикселей
    val density = LocalDensity.current

    // Создаем контейнер Box для центрирования содержимого
    Box(contentAlignment = Alignment.Center) {
        // Основной столбец
        Column(
            modifier = modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    // Измеряем ширину столбца и сохраняем ее в переменную columnWidth
                    columnWidth = with(density) {
                        it.size.width.toDp()
                    }
                }
        ) {
            // Проверяем, что ширина измерена и не равна нулю
            if (columnWidth.value.toInt() != 0) {
                // Вычисляем количество столбцов в ряду на основе ширины столбца и промежутков
                val columns = (columnWidth.value.toInt() + spaceHorizontal) / (itemWidth + spaceHorizontal)

                // Вычисляем количество строк
                var rows = (itemCount / columns)
                if (itemCount.mod(columns) > 0) {
                    rows += 1
                }

                // Проходимся по каждой строке и ряду
                for (row in 0 until rows) {
                    val firstIndex = row * columns

                    // Создаем ряд с элементами
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Проходимся по каждому элементу в ряду
                        for (column in 0 until columns) {
                            val index = firstIndex + column
                            if (index < itemCount) {
                                // Вызываем функцию content для отображения элемента
                                content(index)
                            } else {
                                // Если элементы закончились, добавляем пустое место
                                Spacer(modifier = Modifier.width(itemWidth.dp))
                            }
                        }
                    }
                    // Добавляем вертикальный отступ между строками
                    if (row != rows - 1) {
                        Spacer(modifier = Modifier.height(spaceVertical.dp))
                    }
                }
            }
        }
    }
}
