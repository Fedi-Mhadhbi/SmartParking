package com.fedi.smartparking.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fedi.smartparking.R
import com.fedi.smartparking.features.home.DriverHomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { DriverBottomNavigation(navController) },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.HOME,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable(NavRoutes.HOME) {
                DriverHomeScreen(
                    onParkingClick = { navController.navigate(NavRoutes.EXPLORE) },
                )
            }
            composable(NavRoutes.EXPLORE) { SectionPlaceholder() }
            composable(NavRoutes.BOOKINGS) { SectionPlaceholder() }
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
