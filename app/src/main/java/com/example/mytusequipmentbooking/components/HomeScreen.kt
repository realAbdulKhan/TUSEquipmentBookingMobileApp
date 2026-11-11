
package com.example.mytusequipmentbooking.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mytusequipmentbooking.components.EquipmentCard
import com.example.mytusequipmentbooking.model.Category
import com.example.mytusequipmentbooking.model.Equipment

@Composable
fun HomeScreen(
    equipment: List<Equipment>,
    categories: List<Category>,
    onEquipmentClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Button(
                onClick = onBack
            ) {
                Text("Back to Welcome")
            }

            Text(
                text = "Available Equipment"
            )
        }

        items(
            items = equipment,
            key = { it.id }
        ) { item ->

            val categoryName = categories
                .firstOrNull { it.id == item.categoryId }
                ?.name ?: "Unknown Category"

            EquipmentCard(
                equipment = item,
                categoryName = categoryName,
                onOpen = {
                    onEquipmentClick(item.id)
                }
            )
        }
    }
}
