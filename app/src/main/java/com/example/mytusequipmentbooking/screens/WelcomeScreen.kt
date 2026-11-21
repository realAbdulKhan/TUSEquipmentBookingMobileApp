package com.example.mytusequipmentbooking.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(
    // Callback that runs when the user presses the Browse Equipment button.
    // AppNavigation uses this to navigate to the equipment catalogue screen.
    onBrowseEquipment: () -> Unit
) {

    // Column arranges all the welcome screen elements vertically.
    Column(
        modifier = Modifier
            // Makes the Column take up the full available screen space.
            .fillMaxSize()

            // Adds 24dp of space around the content.
            .padding(24.dp),

        // Aligns the content horizontally in the centre of the screen.
        horizontalAlignment = Alignment.CenterHorizontally,

        // Aligns the content vertically in the centre of the screen.
        verticalArrangement = Arrangement.Center
    ) {

        // Main heading displayed when the user opens the app.
        // headlineMedium uses the typography style provided by MaterialTheme.
        Text(
            text = "Welcome to TUS Equipment Booking",
            style = MaterialTheme.typography.headlineMedium
        )

        // Short description explaining what the application is used for.
        Text(
            text = "Browse and book available college equipment.",

            // Adds 16dp of space above the description.
            modifier = Modifier.padding(top = 16.dp),

            // Uses the bodyLarge typography style for the description.
            style = MaterialTheme.typography.bodyLarge
        )

        // Button that allows the user to move from the welcome screen
        // to the equipment catalogue.
        Button(
            // Calls the onBrowseEquipment callback when pressed.
            // The actual navigation is handled by AppNavigation.
            onClick = onBrowseEquipment,

            // Adds 24dp of space above the button.
            modifier = Modifier.padding(top = 24.dp)
        ) {
            // Text displayed inside the button.
            Text("Browse Equipment")
        }
    }
}