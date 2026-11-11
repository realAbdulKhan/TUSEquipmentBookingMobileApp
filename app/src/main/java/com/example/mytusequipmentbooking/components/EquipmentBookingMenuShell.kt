
package com.example.mytusequipmentbooking.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentBookingMenuShell(
    onWelcomeClick: () -> Unit,
    onBrowseClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("TUS Equipment Booking")
                },
                actions = {
                    TextButton(
                        onClick = {
                            menuExpanded = true
                        }
                    ) {
                        Text("Menu")
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = {
                            menuExpanded = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Welcome") },
                            onClick = {
                                menuExpanded = false
                                onWelcomeClick()
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Browse Equipment") },
                            onClick = {
                                menuExpanded = false
                                onBrowseClick()
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}
