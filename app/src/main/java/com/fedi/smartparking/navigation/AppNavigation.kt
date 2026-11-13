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
import com.fedi.smartparking.features.bookings.BookingsScreen

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
                )
            }
            composable(NavRoutes.EXPLORE) {
                ExploreScreen(
                    onBack = { navController.popBackStack() },
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
                    onBack = { navController.popBackStack() },
                    onConfirm = {
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
            composable(NavRoutes.BOOKINGS) { BookingsScreen() }
            composable(NavRoutes.PROFILE) { SectionPlaceholder() }
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
