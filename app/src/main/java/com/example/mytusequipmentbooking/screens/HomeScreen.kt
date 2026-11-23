
package com.example.mytusequipmentbooking.screens

import androidx.compose.foundation.layout.Column
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

    // List of equipment received from the ViewModel through AppNavigation.
    // Each Equipment object contains its name, description and category ID.
    equipment: List<Equipment>,

    // List of categories used to identify the category belonging to each item.
    categories: List<Category>,

    // Shows whether the application is currently loading database data.
    isLoading: Boolean,

    // Holds an error message if loading the equipment or categories fails.
    // Null means there is currently no error.
    error: String?,

    // Identifies whether the currently signed-in user is an administrator.
    // Administrators can add and delete equipment.
    isAdmin: Boolean,

    // Callback called when the user selects an equipment item.
    // Passes the equipment ID so AppNavigation can open its details screen.
    onEquipmentClick: (Int) -> Unit,

    // Callback used when the user wants to return to the Welcome screen.
    onBack: () -> Unit,

    // Callback used when the user saves new equipment.
    // Passes the new Equipment object to AppNavigation and the ViewModel.
    onAddEquipment: (Equipment) -> Unit,

    // Callback used when the user confirms equipment deletion.
    // Passes the equipment ID to the ViewModel.
    onDeleteEquipment: (Int) -> Unit

) {

    // Controls whether the Add Equipment dialog is displayed.
    // Starts as false, meaning the dialog is hidden.
    var showAddDialog by rememberSaveable {
        mutableStateOf(false)
    }

    // Stores the name entered by the user in the Add Equipment dialog.
    var equipmentName by rememberSaveable {
        mutableStateOf("")
    }

    // Stores the description entered by the user.
    var equipmentDescription by rememberSaveable {
        mutableStateOf("")
    }

    // Stores the ID of the category selected by the user.
    // -1 means that no category has been selected yet.
    var selectedCategoryId by rememberSaveable {
        mutableStateOf(-1)
    }

    // Controls whether the category dropdown menu is open.
    var categoryDropdownExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    // Stores a validation error if the user leaves a required field empty.
    // Null means there is no validation error.
    var validationError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    // Stores the equipment selected for deletion.
    // Null means that no equipment is currently selected for deletion.
    var equipmentToDelete by rememberSaveable {
        mutableStateOf<Equipment?>(null)
    }


    // LazyColumn displays the equipment list in a vertically scrolling layout.
    // It only composes the items that are needed as the user scrolls.
    LazyColumn(
        // Makes the list fill the available screen space.
        modifier = Modifier.fillMaxSize(),

        // Adds 16dp of padding around the list content.
        contentPadding = PaddingValues(16.dp)
    ) {

        // This item contains the buttons and heading displayed above the list.
        item {

            // Button that returns the user to the Welcome screen.
            Button(onClick = onBack) {
                Text("Back to Welcome")
            }

            // Heading displayed above the equipment catalogue.
            Text("Available Equipment")

            // Only administrators can see the Add Equipment button.
            if (isAdmin) {

                // Button that opens the Add Equipment dialog.
                Button(
                    onClick = {

                        // Clears the name field before opening the dialog.
                        equipmentName = ""

                        // Clears the description field.
                        equipmentDescription = ""

                        // Selects the first available category by default.
                        // If there are no categories, the value remains -1.
                        selectedCategoryId =
                            categories.firstOrNull()?.id ?: -1

                        // Clears any previous validation error.
                        validationError = null

                        // Makes the Add Equipment dialog visible.
                        showAddDialog = true
                    },

                    // The button is only enabled when:
                    // 1. The application is not loading.
                    // 2. There is no loading error.
                    // 3. At least one category exists.
                    enabled = !isLoading &&
                            error == null &&
                            categories.isNotEmpty()

                ) {
                    // Text displayed inside the Add Equipment button.
                    Text("Add Equipment")
                }
            }
        }


        // Checks whether the equipment data is still loading.
        if (isLoading) {

            // Displays a loading message while waiting for the database data.
            item {
                Text("Loading equipment...")
            }

            // Checks whether an error occurred while loading the data.
        } else if (error != null) {

            // Displays the error message on the screen.
            item {
                Text("Error: $error")
            }

            // Checks whether the equipment list is empty.
        } else if (equipment.isEmpty()) {

            // Displays a message when there are no equipment records.
            item {
                Text("No equipment available.")
            }

        } else {

            // Displays each equipment object as an item in the scrolling list.
            items(
                items = equipment,

                // Uses the equipment ID as a unique key for each list item.
                // This helps Compose identify individual equipment records.
                key = { it.id }

            ) { item ->

                // Finds the category that matches this equipment's category ID.
                // firstOrNull returns the first matching category, or null if none exists.
                // If no category is found, "Unknown Category" is displayed.
                val categoryName = categories
                    .firstOrNull { it.id == item.categoryId }
                    ?.name ?: "Unknown Category"

                // Reuses the EquipmentCard component to display each equipment item.
                EquipmentCard(
                    // Passes the current equipment object to the card.
                    equipment = item,

                    // Passes the category name found above.
                    categoryName = categoryName,

                    // Runs when the user clicks the equipment details area.
                    onOpen = {
                        // Sends the selected equipment ID to AppNavigation.
                        // AppNavigation uses it to open EquipmentDetailsScreen.
                        onEquipmentClick(item.id)
                    },

                    // Runs when the user presses Delete Equipment on the card.
                    onDelete = {
                        // Stores the selected equipment object.
                        // This causes the delete confirmation dialog to appear.
                        equipmentToDelete = item
                    },

                    // Passes the administrator status to EquipmentCard.
                    // Only administrators can see the Delete Equipment button.
                    isAdmin = isAdmin
                )
            }
        }
    }


    // ADD EQUIPMENT DIALOG
    // This dialog is only displayed when showAddDialog is true.
    if (showAddDialog && isAdmin) {

        // AlertDialog displays a popup for entering new equipment details.
        AlertDialog(
            // Runs if the user dismisses the dialog.
            onDismissRequest = {
                // Hides the Add Equipment dialog.
                showAddDialog = false
            },

            // Title displayed at the top of the dialog.
            title = {
                Text("Add New Equipment")
            },

            // Contains the input fields and category selection.
            text = {
                // Column arranges the dialog contents vertically.
                Column {

                    // Text field for entering the equipment name.
                    OutlinedTextField(
                        // Displays the current equipment name.
                        value = equipmentName,

                        // Updates the name whenever the user types.
                        onValueChange = {
                            equipmentName = it

                            // Clears any previous validation error.
                            validationError = null
                        },

                        // Label displayed on the input field.
                        label = {
                            Text("Equipment Name")
                        },

                        // Keeps the equipment name on a single line.
                        singleLine = true
                    )

                    // Text field for entering the equipment description.
                    OutlinedTextField(
                        // Displays the current description.
                        value = equipmentDescription,

                        // Updates the description whenever the user types.
                        onValueChange = {
                            equipmentDescription = it

                            // Clears any previous validation error.
                            validationError = null
                        },

                        // Label displayed on the input field.
                        label = {
                            Text("Description")
                        }
                    )

                    // Heading displayed above the category selection.
                    Text("Select Category")

                    // Button used to open the category dropdown.
                    Button(
                        onClick = {
                            // Makes the category dropdown visible.
                            categoryDropdownExpanded = true
                        }
                    ) {

                        // Displays the currently selected category name.
                        // If no matching category exists, displays "Choose Category".
                        Text(
                            categories.firstOrNull {
                                it.id == selectedCategoryId
                            }?.name ?: "Choose Category"
                        )
                    }

                    // Dropdown menu containing the available categories.
                    DropdownMenu(
                        // Controls whether the dropdown is visible.
                        expanded = categoryDropdownExpanded,

                        // Closes the dropdown when the user dismisses it.
                        onDismissRequest = {
                            categoryDropdownExpanded = false
                        }

                    ) {

                        // Loops through all available categories.
                        categories.forEach { category ->

                            // Creates one dropdown option for each category.
                            DropdownMenuItem(
                                // Displays the category name.
                                text = {
                                    Text(category.name)
                                },

                                // Runs when the user selects a category.
                                onClick = {
                                    // Saves the selected category ID.
                                    selectedCategoryId = category.id

                                    // Closes the dropdown.
                                    categoryDropdownExpanded = false

                                    // Clears any previous validation error.
                                    validationError = null
                                }
                            )
                        }
                    }

                    // Displays the validation error if one exists.
                    if (validationError != null) {
                        Text(validationError!!)
                    }
                }
            },

            // Defines the Save button at the bottom of the dialog.
            confirmButton = {
                TextButton(
                    onClick = {

                        // Checks whether the equipment name is empty or only contains spaces.
                        if (equipmentName.isBlank()) {

                            // Displays an error if the name is missing.
                            validationError =
                                "Please enter an equipment name."

                            // Checks whether the description is empty.
                        } else if (equipmentDescription.isBlank()) {

                            // Displays an error if the description is missing.
                            validationError =
                                "Please enter a description."

                            // Checks whether a category has been selected.
                        } else if (selectedCategoryId == -1) {

                            // Displays an error if no category has been selected.
                            validationError =
                                "Please select a category."

                        } else {

                            // Creates a new Equipment object using the user's input.
                            val newEquipment = Equipment(

                                // ID is set to 0 because this is a new record.
                                // Room generates the actual primary key when inserted.
                                id = 0,

                                // Stores the selected category ID.
                                categoryId = selectedCategoryId,

                                // Stores the equipment name without extra spaces.
                                name = equipmentName.trim(),

                                // Stores the description without extra spaces.
                                description = equipmentDescription.trim()
                            )

                            // Passes the new equipment object to AppNavigation.
                            // AppNavigation forwards it to the ViewModel for saving.
                            onAddEquipment(newEquipment)

                            // Closes the Add Equipment dialog.
                            showAddDialog = false
                        }
                    }

                ) {
                    // Text displayed inside the Save button.
                    Text("Save")
                }
            },

            // Defines the Cancel button at the bottom of the dialog.
            dismissButton = {
                TextButton(
                    onClick = {
                        // Closes the dialog without saving the equipment.
                        showAddDialog = false
                    }

                ) {
                    // Text displayed inside the Cancel button.
                    Text("Cancel")
                }
            }
        )
    }


    // DELETE CONFIRMATION DIALOG
    // This dialog only appears when equipmentToDelete is not null.
    if (equipmentToDelete != null && isAdmin) {

        // Displays a confirmation popup before deleting equipment.
        AlertDialog(
            // Runs if the user dismisses the dialog.
            onDismissRequest = {
                // Clears the selected equipment.
                // This also hides the confirmation dialog.
                equipmentToDelete = null
            },

            // Title displayed at the top of the confirmation dialog.
            title = {
                Text("Delete Equipment")
            },

            // Displays a message asking the user to confirm deletion.
            // The selected equipment name is included in the message.
            text = {
                Text(
                    "Are you sure you want to delete " +
                            "${equipmentToDelete?.name}?"
                )
            },

            // Defines the Delete confirmation button.
            confirmButton = {
                TextButton(
                    onClick = {

                        // let is only executed if equipmentToDelete is not null.
                        // The selected equipment object is stored in item.
                        equipmentToDelete?.let { item ->

                            // Passes the selected equipment ID to AppNavigation.
                            // The ViewModel and Repository handle the database deletion.
                            onDeleteEquipment(item.id)
                        }

                        // Clears the selected equipment and closes the dialog.
                        equipmentToDelete = null
                    }

                ) {
                    // Text displayed inside the Delete button.
                    Text("Delete")
                }
            },

            // Defines the Cancel button.
            dismissButton = {
                TextButton(
                    onClick = {
                        // Cancels the deletion and closes the dialog.
                        equipmentToDelete = null
                    }

                ) {
                    // Text displayed inside the Cancel button.
                    Text("Cancel")
                }
            }
        )
    }
}
