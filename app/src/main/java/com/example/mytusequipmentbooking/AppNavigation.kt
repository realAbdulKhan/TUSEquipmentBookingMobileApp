
package com.example.mytusequipmentbooking

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mytusequipmentbooking.components.EquipmentBookingMenuShell
import com.example.mytusequipmentbooking.components.MyBookingsScreen
import com.example.mytusequipmentbooking.components.WelcomeScreen
import com.example.mytusequipmentbooking.screens.EquipmentDetailsScreen
import com.example.mytusequipmentbooking.screens.HomeScreen
import com.example.mytusequipmentbooking.viewmodel.EquipmentBookingViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val equipmentViewModel: EquipmentBookingViewModel = viewModel()
    val uiState = equipmentViewModel.uiState.collectAsStateWithLifecycle()

    EquipmentBookingMenuShell(
        selectedRoute = currentRoute ?: "welcome",

        onWelcomeClick = {
            navController.navigate("welcome")
        },

        onBrowseClick = {
            navController.navigate("home")
        },

        onMyBookingsClick = {
            navController.navigate("bookings")
        }

    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.padding(paddingValues)
        ) {

            composable("welcome") {
                WelcomeScreen(
                    onBrowseEquipment = {
                        navController.navigate("home")
                    }
                )
            }

            composable("home") {
                HomeScreen(
                    equipment = uiState.value.equipment,
                    categories = uiState.value.categories,
                    onEquipmentClick = { equipmentId ->
                        navController.navigate("details/$equipmentId")
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = "details/{equipmentId}",
                arguments = listOf(
                    navArgument("equipmentId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->

                val equipmentId =
                    backStackEntry.arguments?.getInt("equipmentId")

                val selectedEquipment =
                    uiState.value.equipment.firstOrNull {
                        it.id == equipmentId
                    }

                val categoryName =
                    uiState.value.categories.firstOrNull {
                        it.id == selectedEquipment?.categoryId
                    }?.name ?: "Unknown Category"

                if (selectedEquipment != null) {
                    EquipmentDetailsScreen(
                        equipment = selectedEquipment,
                        categoryName = categoryName,
                        onBack = {
                            navController.popBackStack()
                        },
                        onBookEquipment = { durationDays, purpose ->

                            val booking = com.example.mytusequipmentbooking.model.Booking(
                                equipmentId = selectedEquipment.id,
                                durationDays = durationDays,
                                purpose = purpose,
                                status = "Pending"
                            )

                            equipmentViewModel.addBooking(booking)

                            navController.popBackStack()
                        }
                    )
                }
            }

            composable("bookings") {
                MyBookingsScreen(
                    bookings = uiState.value.bookings,
                    equipment = uiState.value.equipment,
                    onCancelBooking = { bookingId ->
                        equipmentViewModel.cancelBooking(bookingId)
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
