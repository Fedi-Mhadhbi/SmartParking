package com.fedi.smartparking.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fedi.smartparking.R
import com.fedi.smartparking.features.home.DriverHomeScreen
import com.fedi.smartparking.features.explore.ExploreScreen
import com.fedi.smartparking.features.parking.ParkingDetailsScreen
import com.fedi.smartparking.features.reservation.ReservationScreen
import com.fedi.smartparking.features.reservation.BookingConfirmationScreen
import com.fedi.smartparking.features.reservation.BookingSuccessScreen
import com.fedi.smartparking.features.reservation.DemoReservationStore
import com.fedi.smartparking.features.reservation.ReservationSelection
import com.fedi.smartparking.features.bookings.BookingsScreen
import com.fedi.smartparking.features.bookings.BookingDetailsScreen
import com.fedi.smartparking.features.profile.ProfileScreen
import com.fedi.smartparking.features.profile.LanguageScreen
import com.fedi.smartparking.features.notifications.NotificationsScreen
import com.fedi.smartparking.features.support.HelpSupportScreen
import com.fedi.smartparking.features.payment.PaymentScreen
import com.fedi.smartparking.features.vehicles.MyVehiclesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in setOf(
        NavRoutes.HOME,
        NavRoutes.EXPLORE,
        NavRoutes.BOOKINGS,
        NavRoutes.PROFILE,
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                DriverBottomNavigation(navController)
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AuthRoutes.SPLASH,
            modifier = Modifier.padding(paddingValues),
        ) {
            authNavGraph(
                navController = navController,
                onAuthenticated = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(AuthRoutes.WELCOME) { inclusive = true }
                    }
                },
            )
            composable(NavRoutes.HOME) {
                DriverHomeScreen(
                    onParkingClick = { navController.navigate(NavRoutes.EXPLORE) },
                    onNotificationsClick = { navController.navigate(NavRoutes.NOTIFICATIONS) },
                    onSeeAllClick = { navController.navigate(NavRoutes.EXPLORE) },
                    onVehiclesClick = { navController.navigate(NavRoutes.MY_VEHICLES) },
                )
            }
            composable(NavRoutes.NOTIFICATIONS) {
                NotificationsScreen(onBack = { navController.popBackStack() })
            }
            composable(NavRoutes.EXPLORE) {
                ExploreScreen(
                    onBack = {
                        navController.navigate(NavRoutes.HOME) {
                            popUpTo(NavRoutes.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onParkingClick = { parking ->
                        navController.navigate(NavRoutes.parkingDetails(parking.name))
                    },
                )
            }
            composable(
                route = NavRoutes.PARKING_DETAILS,
                arguments = listOf(navArgument("parkingName") { type = NavType.StringType }),
            ) { entry ->
                ParkingDetailsScreen(
                    parkingName = entry.arguments?.getString("parkingName").orEmpty(),
                    onBack = { navController.popBackStack() },
                    onReserve = {
                        navController.navigate(
                            NavRoutes.reservation(entry.arguments?.getString("parkingName").orEmpty()),
                        )
                    },
                )
            }
            composable(
                route = NavRoutes.RESERVATION,
                arguments = listOf(navArgument("parkingName") { type = NavType.StringType }),
            ) { entry ->
                ReservationScreen(
                    parkingName = entry.arguments?.getString("parkingName").orEmpty(),
                    onBack = {
                        navController.navigate(NavRoutes.HOME) {
                            popUpTo(NavRoutes.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onConfirm = { startAtMillis, durationHours, hourlyPrice, vehicleName, parkingSpot ->
                        DemoReservationStore.add(
                            ReservationSelection(
                                parkingName = entry.arguments?.getString("parkingName").orEmpty(),
                                startAtMillis = startAtMillis,
                                durationHours = durationHours,
                                hourlyPrice = hourlyPrice,
                                vehicleName = vehicleName,
                                parkingSpot = parkingSpot,
                            ),
                        )
                        navController.navigate(
                            NavRoutes.bookingConfirmation(entry.arguments?.getString("parkingName").orEmpty()),
                        )
                    },
                )
            }
            composable(
                route = NavRoutes.BOOKING_CONFIRMATION,
                arguments = listOf(navArgument("parkingName") { type = NavType.StringType }),
            ) { entry ->
                BookingConfirmationScreen(
                    parkingName = entry.arguments?.getString("parkingName").orEmpty(),
                    onBack = { navController.popBackStack() },
                    onConfirm = {
                        navController.navigate(
                            NavRoutes.payment(entry.arguments?.getString("parkingName").orEmpty()),
                        )
                    },
                )
            }
            composable(
                route = NavRoutes.PAYMENT,
                arguments = listOf(navArgument("parkingName") { type = NavType.StringType }),
            ) { entry ->
                PaymentScreen(
                    parkingName = entry.arguments?.getString("parkingName").orEmpty(),
                    onBack = { navController.popBackStack() },
                    onPaymentComplete = {
                        navController.navigate(
                            NavRoutes.bookingSuccess(entry.arguments?.getString("parkingName").orEmpty()),
                        )
                    },
                )
            }
            composable(
                route = NavRoutes.BOOKING_SUCCESS,
                arguments = listOf(navArgument("parkingName") { type = NavType.StringType }),
            ) { entry ->
                BookingSuccessScreen(
                    parkingName = entry.arguments?.getString("parkingName").orEmpty(),
                    onDone = {
                        navController.navigate(NavRoutes.HOME) {
                            popUpTo(NavRoutes.HOME) { inclusive = false }
                        }
                    },
                )
            }
            composable(NavRoutes.BOOKINGS) {
                BookingsScreen(
                    onBookingClick = { parkingName ->
                        navController.navigate(NavRoutes.bookingDetails(parkingName))
                    },
                )
            }
            composable(
                route = NavRoutes.BOOKING_DETAILS,
                arguments = listOf(navArgument("parkingName") { type = NavType.StringType }),
            ) { entry ->
                BookingDetailsScreen(
                    parkingName = entry.arguments?.getString("parkingName").orEmpty(),
                    onBack = { navController.popBackStack() },
                    onShowQr = {
                        navController.navigate(
                            NavRoutes.bookingSuccess(entry.arguments?.getString("parkingName").orEmpty()),
                        )
                    },
                )
            }
            composable(NavRoutes.PROFILE) {
                ProfileScreen(
                    onHelpClick = { navController.navigate(NavRoutes.HELP) },
                    onBookingsClick = { navController.navigate(NavRoutes.BOOKINGS) },
                    onVehiclesClick = { navController.navigate(NavRoutes.MY_VEHICLES) },
                    onNotificationsClick = { navController.navigate(NavRoutes.NOTIFICATIONS) },
                    onLanguageClick = { navController.navigate(NavRoutes.LANGUAGE) },
                    onLogout = {
                        com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                        navController.navigate(AuthRoutes.WELCOME) {
                            popUpTo(NavRoutes.HOME) { inclusive = true }
                        }
                    },
                )
            }
            composable(NavRoutes.MY_VEHICLES) {
                MyVehiclesScreen(onBack = { navController.popBackStack() })
            }
            composable(NavRoutes.HELP) {
                HelpSupportScreen(onBack = { navController.popBackStack() })
            }
            composable(NavRoutes.LANGUAGE) {
                LanguageScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
private fun SectionPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.section_coming_soon),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
