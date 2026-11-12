
package com.example.mytusequipmentbooking.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.example.mytusequipmentbooking.components.EquipmentCard
import com.example.mytusequipmentbooking.model.Category
import com.example.mytusequipmentbooking.model.Equipment

@Composable
fun HomeScreen(
    equipment: List<Equipment>,
    categories: List<Category>,
    isLoading: Boolean,
    error: String?,
    onEquipmentClick: (Int) -> Unit,
    onBack: () -> Unit,
    onAddEquipment: (Equipment) -> Unit
) {

    var showAddDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var equipmentName by rememberSaveable {
        mutableStateOf("")
    }

    var equipmentDescription by rememberSaveable {
        mutableStateOf("")
    }

    var selectedCategoryId by rememberSaveable {
        mutableStateOf(-1)
    }

    var categoryDropdownExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var validationError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

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

            Text("Available Equipment")

            Button(
                onClick = {
                    equipmentName = ""
                    equipmentDescription = ""
                    selectedCategoryId = categories.firstOrNull()?.id ?: -1
                    validationError = null
                    showAddDialog = true
                },
                enabled = !isLoading && error == null && categories.isNotEmpty()
            ) {
                Text("Add Equipment")
            }
        }

        if (isLoading) {
            item {
                Text("Loading equipment...")
            }
        } else if (error != null) {
            item {
                Text("Error: $error")
            }
        } else if (equipment.isEmpty()) {
            item {
                Text("No equipment available.")
            }
        } else {
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

    // ADD EQUIPMENT DIALOG
    if (showAddDialog) {

        AlertDialog(
            onDismissRequest = {
                showAddDialog = false
            },

            title = {
                Text("Add New Equipment")
            },

            text = {

                androidx.compose.foundation.layout.Column {

                    OutlinedTextField(
                        value = equipmentName,
                        onValueChange = {
                            equipmentName = it
                            validationError = null
                        },
                        label = {
                            Text("Equipment Name")
                        },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = equipmentDescription,
                        onValueChange = {
                            equipmentDescription = it
                            validationError = null
                        },
                        label = {
                            Text("Description")
                        }
                    )

                    Text("Select Category")

                    Button(
                        onClick = {
                            categoryDropdownExpanded = true
                        }
                    ) {
                        Text(
                            categories.firstOrNull {
                                it.id == selectedCategoryId
                            }?.name ?: "Choose Category"
                        )
                    }

                    DropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = {
                            categoryDropdownExpanded = false
                        }
                    ) {
                        categories.forEach { category ->

                            DropdownMenuItem(
                                text = {
                                    Text(category.name)
                                },
                                onClick = {
                                    selectedCategoryId = category.id
                                    categoryDropdownExpanded = false
                                    validationError = null
                                }
                            )
                        }
                    }

                    if (validationError != null) {
                        Text(validationError!!)
                    }
                }
            },

            confirmButton = {
                TextButton(
                    onClick = {

                        if (equipmentName.isBlank()) {
                            validationError = "Please enter an equipment name."
                        } else if (equipmentDescription.isBlank()) {
                            validationError = "Please enter a description."
                        } else if (selectedCategoryId == -1) {
                            validationError = "Please select a category."
                        } else {

                            val newEquipment = Equipment(
                                id = 0,
                                categoryId = selectedCategoryId,
                                name = equipmentName.trim(),
                                description = equipmentDescription.trim()
                            )

                            onAddEquipment(newEquipment)

                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Save")
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        showAddDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
