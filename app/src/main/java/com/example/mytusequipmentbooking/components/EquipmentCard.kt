package com.example.mytusequipmentbooking.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mytusequipmentbooking.model.Equipment

@Composable
fun EquipmentCard(
    equipment: Equipment,
    categoryName: String,
    onOpen: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpen() }
            ) {
                Text(
                    text = equipment.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = equipment.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Button(
                onClick = onDelete
            ) {
                Text("Delete Equipment")
            }
        }
    }
}