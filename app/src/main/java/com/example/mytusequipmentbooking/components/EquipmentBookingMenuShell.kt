
package com.example.mytusequipmentbooking.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mytusequipmentbooking.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentBookingMenuShell(
    selectedRoute: String,
    onWelcomeClick: () -> Unit,
    onBrowseClick: () -> Unit,
    onMyBookingsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    userEmail: String?,
    onLogout: () -> Unit,
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

                if (userEmail != null) {
                    Text(
                        text = "Signed in as: $userEmail",
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                NavigationDrawerItem(
                    label = { Text("Welcome") },
                    selected = selectedRoute == "welcome",
                    onClick = {
                        scope.launch { drawerState.close() }
                        onWelcomeClick()
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Browse Equipment") },
                    selected = selectedRoute == "home",
                    onClick = {
                        scope.launch { drawerState.close() }
                        onBrowseClick()
                    }
                )

                NavigationDrawerItem(
                    label = { Text("My Bookings") },
                    selected = selectedRoute == "bookings",
                    onClick = {
                        scope.launch { drawerState.close() }
                        onMyBookingsClick()
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = selectedRoute == "settings",
                    onClick = {
                        scope.launch { drawerState.close() }
                        onSettingsClick()
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogout()
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
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.ic_menu
                                ),
                                contentDescription = "Open menu"
                            )
                        }
                    }
                )
            },

            bottomBar = {
                NavigationBar {

                    NavigationBarItem(
                        selected = selectedRoute == "welcome",
                        onClick = onWelcomeClick,
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.ic_home
                                ),
                                contentDescription = "Welcome"
                            )
                        },
                        label = { Text("Welcome") }
                    )

                    NavigationBarItem(
                        selected = selectedRoute == "home",
                        onClick = onBrowseClick,
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.ic_equipment
                                ),
                                contentDescription = "Equipment"
                            )
                        },
                        label = { Text("Equipment") }
                    )

                    NavigationBarItem(
                        selected = selectedRoute == "bookings",
                        onClick = onMyBookingsClick,
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.ic_bookings
                                ),
                                contentDescription = "Bookings"
                            )
                        },
                        label = { Text("Bookings") }
                    )

                    NavigationBarItem(
                        selected = selectedRoute == "settings",
                        onClick = onSettingsClick,
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.ic_settings
                                ),
                                contentDescription = "Settings"
                            )
                        },
                        label = { Text("Settings") }
                    )
                }
            }

        ) { paddingValues ->

            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
            ) {
                content(PaddingValues(0.dp))
            }
        }
    }
}
