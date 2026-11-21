
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

    // The selected equipment passed from AppNavigation.
    // Contains the equipment name, ID, category ID and description.
    equipment: Equipment,

    // The category name is passed separately so it can be displayed on screen.
    categoryName: String,

    // Callback used when the user presses the Back button.
    onBack: () -> Unit,

    // Callback used when the user saves a booking.
    // It passes the selected duration in days and the booking purpose.
    onBookEquipment: (Int, String) -> Unit

) {

    // Stores the selected booking duration.
    // The default duration is 1 day.
    // rememberSaveable keeps the value when the screen is recreated.
    var durationDays by rememberSaveable {
        mutableStateOf(1)
    }

    // Stores the purpose entered by the user.
    // Starts as an empty string.
    var purpose by rememberSaveable {
        mutableStateOf("")
    }

    // Controls whether the booking duration dropdown is open or closed.
    // It starts closed.
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    // Stores an optional validation error message.
    // Null means there is currently no error to display.
    var errorMessage by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    // Column arranges all the screen components vertically.
    Column(
        modifier = Modifier
            // Makes the screen take up all available space.
            .fillMaxSize()

            // Adds 16dp of spacing around the screen content.
            .padding(16.dp),

        // Adds 12dp of vertical spacing between the components.
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Displays the name of the selected equipment.
        Text(
            text = equipment.name,

            // Uses a large headline style for the equipment name.
            style = MaterialTheme.typography.headlineMedium
        )

        // Displays the category of the selected equipment.
        Text(
            text = "Category: $categoryName",

            // Uses the medium title text style.
            style = MaterialTheme.typography.titleMedium
        )

        // Displays the equipment description.
        Text(
            text = equipment.description,

            // Uses the large body text style.
            style = MaterialTheme.typography.bodyLarge
        )

        // Heading displayed above the duration selection.
        Text("Booking Duration")

        // Button that opens the duration dropdown menu.
        TextButton(onClick = {
            // Sets expanded to true so the dropdown becomes visible.
            expanded = true
        }) {

            // Displays the currently selected booking duration.
            Text("$durationDays day(s)")
        }

        // Dropdown menu containing the available booking durations.
        DropdownMenu(
            // Controls whether the dropdown menu is visible.
            expanded = expanded,

            // Closes the dropdown if the user dismisses it.
            onDismissRequest = {
                expanded = false
            }
        ) {

            // Creates a dropdown item for each duration from 1 to 5 days.
            (1..5).forEach { days ->

                DropdownMenuItem(
                    // Displays the duration option, for example 1 day or 3 days.
                    text = {
                        Text("$days day(s)")
                    },

                    // Runs when the user selects a duration.
                    onClick = {
                        // Updates the selected booking duration.
                        durationDays = days

                        // Closes the dropdown after selection.
                        expanded = false
                    }
                )
            }
        }

        // Text field where the user enters why they need the equipment.
        OutlinedTextField(
            // Displays the current purpose value inside the text field.
            value = purpose,

            // Updates the purpose whenever the user types.
            onValueChange = {
                purpose = it

                // Clears the previous validation error when the user edits the field.
                errorMessage = null
            },

            // Label displayed on the text field.
            label = {
                Text("Purpose of booking")
            },

            modifier = Modifier
                // Makes the text field use the full available width.
                .fillMaxWidth()

                // Sets the minimum and maximum height of the text field.
                .heightIn(min = 56.dp, max = 120.dp),

            // Allows the purpose to be entered over multiple lines.
            singleLine = false
        )

        // Only displays this section when an error message exists.
        if (errorMessage != null) {

            Text(
                // Displays the current validation error.
                text = errorMessage!!,

                // Uses the error colour from the application theme.
                color = MaterialTheme.colorScheme.error
            )
        }

        // Button used to submit the equipment booking.
        Button(
            onClick = {

                // Checks whether the user has entered a purpose.
                if (purpose.isBlank()) {

                    // Displays an error if the purpose is empty or contains only spaces.
                    errorMessage = "Please enter a purpose for your booking."

                } else {

                    // If validation passes, calls the booking callback.
                    // Passes the selected duration and purpose to AppNavigation.
                    // trim() removes unnecessary spaces at the beginning and end.
                    onBookEquipment(durationDays, purpose.trim())
                }
            },

            // Makes the Save Booking button use the full available width.
            modifier = Modifier.fillMaxWidth()

        ) {

            // Text displayed inside the booking button.
            Text("Save Booking")
        }

        // Button that allows the user to return to the previous screen.
        Button(
            // Calls the onBack callback supplied by AppNavigation.
            onClick = onBack,

            // Makes the Back button use the full available width.
            modifier = Modifier.fillMaxWidth()

        ) {

            // Text displayed inside the Back button.
            Text("Back")
        }
    }}
