
package com.example.mytusequipmentbooking.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mytusequipmentbooking.model.Booking
import com.example.mytusequipmentbooking.model.Equipment

@Composable
fun MyBookingsScreen(
    // List of bookings passed from AppNavigation.
    // These bookings come from the ViewModel and are retrieved from the Room database.
    bookings: List<Booking>,

    // List of all equipment.
    // Used to find the equipment name that belongs to each booking.
    equipment: List<Equipment>,

    // shows whether the booking data is currently loading.
    isLoading: Boolean,

    // Holds an error message if loading the bookings fails.
    // It is nullable because there might not be an error.
    error: String?,

    // Callback used when the user confirms that they want to cancel a booking.
    // The booking ID is passed back to AppNavigation and then to the ViewModel.
    onCancelBooking: (Int) -> Unit,

    // Callback used by the Back button to return to the previous screen.
    onBack: () -> Unit
) {
    // Stores the booking that the user has selected to cancel.
    // It starts as null because no booking is selected initially.
    // When a booking is selected, this value changes and displays the confirmation dialog.
    var bookingToCancel by remember {
        mutableStateOf<Booking?>(null)
    }

    // Column arranges the main screen content vertically.
    // fillMaxSize makes it take up the available screen space.
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // LazyColumn displays the bookings in a scrollable list.
        // It only creates list items when they are needed on screen.
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Displays the heading at the top of the bookings screen.
            item {
                Text("My Bookings")
            }

            // Checks whether the booking data is still loading.
            // If it is, a loading message is displayed.
            if (isLoading) {
                item {
                    Text("Loading bookings...")
                }

                // Checks whether an error occurred while loading the bookings.
                // If there is an error, its message is displayed on screen.
            } else if (error != null) {
                item {
                    Text("Error: $error")
                }

                // Checks whether the bookings list is empty.
                // This message appears when the user has not made any bookings.
            } else if (bookings.isEmpty()) {
                item {
                    Text("You have no bookings yet.")
                }

                // If the data has loaded successfully and bookings exist,
                // items() creates a list entry for each booking.
            } else {
                items(
                    items = bookings,

                    // Uses the booking ID as the unique key for each list item.
                    key = { it.id }
                ) { booking ->

                    // Finds the equipment that matches the equipment ID
                    // stored inside the current booking.
                    val selectedEquipment = equipment.firstOrNull {
                        it.id == booking.equipmentId
                    }

                    // Card provides a separate visual container for each booking.
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        // Column arranges the booking information vertically.
                        // padding adds space inside the card.
                        // spacedBy adds space between each piece of information.
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            // Displays the name of the equipment associated with this booking.
                            // The safe-call operator ?. checks whether equipment was found.
                            // If no matching equipment exists, "Unknown Equipment" is displayed.
                            Text(
                                text = selectedEquipment?.name
                                    ?: "Unknown Equipment"
                            )

                            // Displays how many days the equipment was booked for.
                            Text("Duration: ${booking.durationDays} day(s)")

                            // Displays the reason the user entered when making the booking.
                            Text("Purpose: ${booking.purpose}")

                            // Displays the current booking status.
                            // For example: Pending or Cancelled.
                            Text("Status: ${booking.status}")

                            // Only displays the Cancel Booking button
                            // if the booking has not already been cancelled.
                            if (booking.status != "Cancelled") {

                                Button(
                                    onClick = {
                                        // Stores the booking selected by the user.
                                        // This changes bookingToCancel from null to this booking.
                                        // The confirmation dialog will then appear.
                                        bookingToCancel = booking
                                    }
                                ) {
                                    Text("Cancel Booking")
                                }
                            }
                        }
                    }
                }
            }
        }

        // Back button displayed underneath the scrollable bookings list.
        // Calls onBack to return to the previous screen.
        Button(
            onClick = onBack,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Back")
        }
    }

    // Checks whether a booking has been selected for cancellation.
    // If bookingToCancel is not null, the confirmation dialog is displayed.
    if (bookingToCancel != null) {

        AlertDialog(

            // Called if the user dismisses the dialog.
            // Resets the selected booking to null and closes the dialog.
            onDismissRequest = {
                bookingToCancel = null
            },

            // Title displayed at the top of the confirmation dialog.
            title = {
                Text("Cancel Booking")
            },

            // Message asking the user to confirm their cancellation.
            text = {
                Text("Are you sure you want to cancel this booking?")
            },

            // Confirm button used when the user wants to cancel the booking.
            confirmButton = {
                TextButton(
                    onClick = {

                        // Checks whether a booking is selected.
                        // If it is, the booking ID is passed to onCancelBooking.
                        // This callback connects to AppNavigation and the ViewModel,
                        // which handles updating the booking status in the database.
                        bookingToCancel?.let {
                            onCancelBooking(it.id)
                        }

                        // Resets the selected booking to null.
                        // This closes the confirmation dialog.
                        bookingToCancel = null
                    }
                ) {
                    Text("Confirm")
                }
            },

            // Dismiss button allows the user to keep their booking.
            dismissButton = {
                TextButton(
                    onClick = {
                        // Resets the selected booking to null without cancelling it.
                        // The confirmation dialog closes and the booking remains unchanged.
                        bookingToCancel = null
                    }
                ) {
                    Text("Keep Booking")
                }
            }
        )
    }
}