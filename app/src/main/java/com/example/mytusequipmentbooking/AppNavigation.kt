
package com.example.mytusequipmentbooking

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mytusequipmentbooking.components.EquipmentBookingMenuShell
import com.example.mytusequipmentbooking.model.Booking
import com.example.mytusequipmentbooking.screens.EquipmentDetailsScreen
import com.example.mytusequipmentbooking.screens.HomeScreen
import com.example.mytusequipmentbooking.screens.LoginScreen
import com.example.mytusequipmentbooking.screens.MyBookingsScreen
import com.example.mytusequipmentbooking.screens.SettingsScreen
import com.example.mytusequipmentbooking.screens.WelcomeScreen
import com.example.mytusequipmentbooking.viewmodel.AuthViewModel
import com.example.mytusequipmentbooking.viewmodel.EquipmentBookingViewModel

@Composable
fun AppNavigation(
    darkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {

    val navController = rememberNavController()

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val authViewModel: AuthViewModel = viewModel()
    val authState = authViewModel.uiState.collectAsStateWithLifecycle()

    val equipmentViewModel: EquipmentBookingViewModel = viewModel()
    val uiState = equipmentViewModel.uiState.collectAsStateWithLifecycle()

    val isLoggedIn = authState.value.uid != null

    LaunchedEffect(authState.value.uid) {
        equipmentViewModel.setUserUid(authState.value.uid)
    }

    LaunchedEffect(isLoggedIn, currentRoute) {

        if (isLoggedIn && currentRoute == "login") {

            navController.navigate("welcome") {
                popUpTo("login") {
                    inclusive = true
                }
                launchSingleTop = true
            }

        } else if (!isLoggedIn && currentRoute != "login") {

            navController.navigate("login") {
                popUpTo("welcome") {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {

            LoginScreen(
                isBusy = authState.value.isBusy,
                error = authState.value.error,

                onLogin = { email, password ->
                    authViewModel.login(email, password)
                },

                onRegister = { email, password, confirmPassword ->
                    authViewModel.register(
                        email,
                        password,
                        confirmPassword
                    )
                }
            )
        }

        composable("welcome") {

            AuthenticatedScreen(
                selectedRoute = currentRoute ?: "welcome",
                userEmail = authState.value.email,
                onWelcomeClick = {
                    navController.navigate("welcome") {
                        launchSingleTop = true
                    }
                },
                onBrowseClick = {
                    navController.navigate("home") {
                        launchSingleTop = true
                    }
                },
                onMyBookingsClick = {
                    navController.navigate("bookings") {
                        launchSingleTop = true
                    }
                },
                onSettingsClick = {
                    navController.navigate("settings") {
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    authViewModel.logout()
                }
            ) { paddingValues ->

                WelcomeScreen(
                    onBrowseEquipment = {
                        navController.navigate("home")
                    }
                )
            }
        }

        composable("home") {

            AuthenticatedScreen(
                selectedRoute = currentRoute ?: "home",
                userEmail = authState.value.email,
                onWelcomeClick = {
                    navController.navigate("welcome") {
                        launchSingleTop = true
                    }
                },
                onBrowseClick = {
                    navController.navigate("home") {
                        launchSingleTop = true
                    }
                },
                onMyBookingsClick = {
                    navController.navigate("bookings") {
                        launchSingleTop = true
                    }
                },
                onSettingsClick = {
                    navController.navigate("settings") {
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    authViewModel.logout()
                }
            ) { paddingValues ->

                HomeScreen(
                    equipment = uiState.value.equipment,
                    categories = uiState.value.categories,
                    isLoading = uiState.value.isLoading,
                    error = uiState.value.loadError,

                    onEquipmentClick = { equipmentId ->
                        navController.navigate("details/$equipmentId")
                    },

                    onBack = {
                        navController.popBackStack()
                    },

                    onAddEquipment = { newEquipment ->
                        equipmentViewModel.addEquipment(newEquipment)
                    },

                    onDeleteEquipment = { equipmentId ->
                        equipmentViewModel.deleteEquipment(equipmentId)
                    }
                )
            }
        }

        composable(
            route = "details/{equipmentId}",
            arguments = listOf(
                navArgument("equipmentId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            AuthenticatedScreen(
                selectedRoute = currentRoute ?: "home",
                userEmail = authState.value.email,
                onWelcomeClick = {
                    navController.navigate("welcome") {
                        launchSingleTop = true
                    }
                },
                onBrowseClick = {
                    navController.navigate("home") {
                        launchSingleTop = true
                    }
                },
                onMyBookingsClick = {
                    navController.navigate("bookings") {
                        launchSingleTop = true
                    }
                },
                onSettingsClick = {
                    navController.navigate("settings") {
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    authViewModel.logout()
                }
            ) { paddingValues ->

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

                            val userUid = authState.value.uid

                            if (userUid != null) {

                                val booking = Booking(
                                    equipmentId = selectedEquipment.id,
                                    userUid = userUid,
                                    durationDays = durationDays,
                                    purpose = purpose,
                                    status = "Pending"
                                )

                                equipmentViewModel.addBooking(booking)
                                navController.popBackStack()
                            }
                        }
                    )
                }
            }
        }

        composable("bookings") {

            AuthenticatedScreen(
                selectedRoute = currentRoute ?: "bookings",
                userEmail = authState.value.email,
                onWelcomeClick = {
                    navController.navigate("welcome") {
                        launchSingleTop = true
                    }
                },
                onBrowseClick = {
                    navController.navigate("home") {
                        launchSingleTop = true
                    }
                },
                onMyBookingsClick = {
                    navController.navigate("bookings") {
                        launchSingleTop = true
                    }
                },
                onSettingsClick = {
                    navController.navigate("settings") {
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    authViewModel.logout()
                }
            ) { paddingValues ->

                MyBookingsScreen(
                    bookings = uiState.value.bookings,
                    equipment = uiState.value.equipment,
                    isLoading = uiState.value.isLoading,
                    error = uiState.value.loadError,

                    onCancelBooking = { bookingId ->
                        equipmentViewModel.cancelBooking(bookingId)
                    },

                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable("settings") {

            AuthenticatedScreen(
                selectedRoute = "settings",
                userEmail = authState.value.email,
                onWelcomeClick = {
                    navController.navigate("welcome") {
                        launchSingleTop = true
                    }
                },
                onBrowseClick = {
                    navController.navigate("home") {
                        launchSingleTop = true
                    }
                },
                onMyBookingsClick = {
                    navController.navigate("bookings") {
                        launchSingleTop = true
                    }
                },
                onSettingsClick = {
                    navController.navigate("settings") {
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    authViewModel.logout()
                }
            ) { paddingValues ->

                SettingsScreen(
                    darkTheme = darkTheme,
                    onDarkThemeChange = onThemeChange
                )
            }
        }
    }
}

@Composable
private fun AuthenticatedScreen(
    selectedRoute: String,
    userEmail: String?,
    onWelcomeClick: () -> Unit,
    onBrowseClick: () -> Unit,
    onMyBookingsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogout: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {

    EquipmentBookingMenuShell(
        selectedRoute = selectedRoute,
        userEmail = userEmail,
        onWelcomeClick = onWelcomeClick,
        onBrowseClick = onBrowseClick,
        onMyBookingsClick = onMyBookingsClick,
        onSettingsClick = onSettingsClick,
        onLogout = onLogout
    ) { paddingValues ->

        content(paddingValues)
    }
}
