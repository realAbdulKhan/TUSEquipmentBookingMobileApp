
package com.example.mytusequipmentbooking.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mytusequipmentbooking.model.Equipment

@Composable
fun EquipmentDetailsScreen(
    equipment: Equipment,
    categoryName: String,
    onBack: () -> Unit,
    onBookEquipment: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = equipment.name,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Category: $categoryName",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = equipment.description,
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            onClick = onBookEquipment
        ) {
            Text("Book Equipment")
        }

        Button(
            onClick = onBack
        ) {
            Text("Back")
        }
    }
}
