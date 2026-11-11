
package com.example.mytusequipmentbooking.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mytusequipmentbooking.model.Equipment

@Composable
fun EquipmentDetailsScreen(
    equipment: Equipment,
    categoryName: String,
    onBack: () -> Unit,
    onBookEquipment: (Int, String) -> Unit
) {
    var durationDays by rememberSaveable { mutableStateOf(1) }
    var purpose by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

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

        Text("Booking Duration")

        TextButton(onClick = { expanded = true }) {
            Text("$durationDays day(s)")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            (1..5).forEach { days ->
                DropdownMenuItem(
                    text = { Text("$days day(s)") },
                    onClick = {
                        durationDays = days
                        expanded = false
                    }
                )
            }
        }

        OutlinedTextField(
            value = purpose,
            onValueChange = {
                purpose = it
                errorMessage = null
            },
            label = { Text("Purpose of booking") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp, max = 120.dp),
            singleLine = false
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                if (purpose.isBlank()) {
                    errorMessage = "Please enter a purpose for your booking."
                } else {
                    onBookEquipment(durationDays, purpose.trim())
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Booking")
        }

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
