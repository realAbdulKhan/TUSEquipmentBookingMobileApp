
package com.example.mytusequipmentbooking

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mytusequipmentbooking.components.WelcomeScreen
import com.example.mytusequipmentbooking.screens.EquipmentDetailsScreen
import com.example.mytusequipmentbooking.screens.HomeScreen
import com.example.mytusequipmentbooking.viewmodel.EquipmentBookingViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val equipmentViewModel: EquipmentBookingViewModel = viewModel()

    val uiState = equipmentViewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = "welcome"
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

            val equipmentId = backStackEntry.arguments
                ?.getInt("equipmentId")

            val selectedEquipment = uiState.value.equipment
                .firstOrNull { it.id == equipmentId }

            val categoryName = uiState.value.categories
                .firstOrNull { it.id == selectedEquipment?.categoryId }
                ?.name ?: "Unknown Category"

            if (selectedEquipment != null) {
                EquipmentDetailsScreen(
                    equipment = selectedEquipment,
                    categoryName = categoryName,
                    onBack = {
                        navController.popBackStack()
                    },
                    onBookEquipment = {
                        // Booking navigation will be added later
                    }
                )
            }
        }
    }
}
