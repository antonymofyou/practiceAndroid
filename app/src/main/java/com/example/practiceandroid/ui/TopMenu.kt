package com.example.practiceandroid.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practiceandroid.R
import com.example.practiceandroid.viewModels.TopMenuViewModel

data class MenuItem(val icon: Int, val name: String)

@Composable
fun TopMenu(topMenuViewModel: TopMenuViewModel = viewModel()) {
    val selectedItem by topMenuViewModel.selectedItem.collectAsState()
    val menuItems = listOf(
        MenuItem(icon = R.drawable.ic_launcher_foreground, name = "Мои данные"),
        MenuItem(icon = R.drawable.ic_launcher_foreground, name = "Расписание"),
        MenuItem(icon = R.drawable.ic_launcher_foreground, name = "Полезное"),
        MenuItem(icon = R.drawable.ic_launcher_foreground, name = "Рейтинг")
    )
    ListTopMenu(menuItems = menuItems, selectedItem = selectedItem) { index ->
        topMenuViewModel.selectItem(index)
    }
}

@Composable
@Preview
fun TopMenuPreview(){
    TopMenu()
}

@Composable
fun ListTopMenu(menuItems: List<MenuItem>, selectedItem: Int, onItemSelected: (Int) -> Unit) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(menuItems.size) { index ->
            ListMenuItem(
                icon = menuItems[index].icon,
                name = menuItems[index].name,
                isSelected = selectedItem == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}

@Composable
fun ListMenuItem(icon: Int, name: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFF6200EE) else Color.White
    val contentColor = Color.White
    val borderColor = if (isSelected) Color.Transparent else Color.Gray
    val tintColor = if (isSelected) Color.White else Color.Gray

    Box(
        modifier = Modifier
            .background(
                backgroundColor,
                RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .height(70.dp)
            .pointerInput(Unit) { detectTapGestures(onTap = { onClick() }) }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = name,
                tint = tintColor,
                )
            if (isSelected) {
                Text(
                    text = name,
                    color = contentColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}