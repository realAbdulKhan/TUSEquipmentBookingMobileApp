package com.example.mytusequipmentbooking.components
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mytusequipmentbooking.model.Booking
import com.example.mytusequipmentbooking.model.Equipment

@Composable
fun MyBookingsScreen(
    bookings: List<Booking>,
    equipment: List<Equipment>,
    onCancelBooking: (Int) -> Unit,
    onBack: () -> Unit
) {
    var bookingToCancel by remember {
        mutableStateOf<Booking?>(null)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("My Bookings")
            }

            if (bookings.isEmpty()) {
                item {
                    Text("You have no bookings yet.")
                }
            }

            items(
                items = bookings,
                key = { it.id }
            ) { booking ->val selectedEquipment = equipment
                .firstOrNull { it.id == booking.equipmentId }

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = selectedEquipment?.name
                                ?: "Unknown Equipment"
                        )

                        Text("Duration: ${booking.durationDays} day(s)")

                        Text("Purpose: ${booking.purpose}")

                        Text("Status: ${booking.status}")

                        if (booking.status != "Cancelled") {
                            Button(
                                onClick = {
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

        Button(
            onClick = onBack,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Back")
        }
    }

    if (bookingToCancel != null) {
        AlertDialog(
            onDismissRequest = {
                bookingToCancel = null
            },
            title = {
                Text("Cancel Booking")
            },
            text = {
                Text("Are you sure you want to cancel this booking?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        bookingToCancel?.let {
                            onCancelBooking(it.id)
                        }
                        bookingToCancel = null
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        bookingToCancel = null
                    }
                ) {
                    Text("Keep Booking")
                }
            }
        )
    }
}
