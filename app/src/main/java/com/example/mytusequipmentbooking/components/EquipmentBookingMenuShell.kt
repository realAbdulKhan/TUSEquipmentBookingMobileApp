
package com.example.mytusequipmentbooking.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentBookingMenuShell(
    selectedRoute: String,
    onWelcomeClick: () -> Unit,
    onBrowseClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    BackHandler(
        enabled = drawerState.isOpen
    ) {
        scope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "TUS Equipment Booking",
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 24.dp
                    ),
                    style = MaterialTheme.typography.titleLarge
                )

                NavigationDrawerItem(
                    label = {
                        Text("Welcome")
                    },
                    selected = selectedRoute == "welcome",
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        onWelcomeClick()
                    }
                )

                NavigationDrawerItem(
                    label = {
                        Text("Browse Equipment")
                    },
                    selected = selectedRoute == "home",
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        onBrowseClick()
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("TUS Equipment Booking")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Text("☰")
                        }
                    }
                )
            }
        ) { paddingValues ->
            content(paddingValues)
        }
    }
}
