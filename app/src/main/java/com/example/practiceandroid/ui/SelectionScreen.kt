import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practiceandroid.R
import com.example.practiceandroid.viewModels.SelectionScreenViewModel
import ru.nasotku.nasotkuapp.theme.components.NoLazyGrid

enum class CellState {
    UNCHECKED, CHECKED, INTERMEDIATE
}

data class Cell(val id: Int, var state: CellState)


@Composable
fun GridCell(cell: Cell, onClick: () -> Unit) {
    //выбираем цвет иконки
    val color = when (cell.state) {
        CellState.UNCHECKED -> Color.Red
        CellState.CHECKED -> Color.Green
        CellState.INTERMEDIATE -> Color(0xFFFFA500)
    }

    //создаем ячейку
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(60.dp)
            .border(2.dp, Color.Black)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        //добавляем иконку к ячейке
        Icon(painter = when (cell.state){
            CellState.UNCHECKED -> painterResource(R.drawable.ic_unchecked)
            CellState.CHECKED -> painterResource(R.drawable.ic_check_mark)
            CellState.INTERMEDIATE -> painterResource(R.drawable.ic_in_progress)
                                        },
            contentDescription = null,
            tint = color

        )
    }
}

@Composable
fun GridView(cells: List<Cell>, viewModel: SelectionScreenViewModel = viewModel()) {
    //добавляем список ячеек
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        NoLazyGrid(
            itemWidth = 68, // если выбрать другое значение, то отображается некорректно
            itemCount = cells.size,
            spaceHorizontal = 5,
            spaceVertical = 5,
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) { index ->
            GridCell(cells[index]) {
                viewModel.onCellClick(cells[index]) //при нажатии показываем Popup
            }
        }

        //если showPopup и selectedCell не null, то показываем Popup
        if (viewModel.showPopup && viewModel.selectedCell != null) {
            //создаем Popup
            Popup(alignment = Alignment.Center, onDismissRequest = { viewModel.dismissPopup() }) {
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    //добавляем содержимое в Popup
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Урок ${viewModel.selectedCell!!.id}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.dismissPopup() }) {
                            Text("Перейти")
                        }
                    }
                }
            }
        }
    }
}



@Preview
@Composable
fun Preview() {
        GridView(
            listOf(
                Cell(1, CellState.CHECKED), Cell(2, CellState.UNCHECKED),
                Cell(3, CellState.CHECKED), Cell(4, CellState.INTERMEDIATE),
                Cell(5, CellState.UNCHECKED), Cell(6, CellState.INTERMEDIATE),
                Cell(7, CellState.INTERMEDIATE), Cell(8, CellState.CHECKED),
                Cell(9, CellState.UNCHECKED), Cell(10, CellState.CHECKED),
                Cell(11, CellState.CHECKED), Cell(12, CellState.UNCHECKED),
                Cell(13, CellState.UNCHECKED), Cell(14, CellState.INTERMEDIATE),
                Cell(7, CellState.INTERMEDIATE), Cell(8, CellState.CHECKED),
                Cell(9, CellState.UNCHECKED), Cell(10, CellState.CHECKED),
                Cell(11, CellState.CHECKED), Cell(12, CellState.UNCHECKED),
                Cell(13, CellState.UNCHECKED), Cell(14, CellState.INTERMEDIATE),
                Cell(15, CellState.INTERMEDIATE), Cell(16, CellState.INTERMEDIATE),
                Cell(17, CellState.INTERMEDIATE), Cell(18, CellState.INTERMEDIATE),
                Cell(19, CellState.INTERMEDIATE)
            )
        )
}