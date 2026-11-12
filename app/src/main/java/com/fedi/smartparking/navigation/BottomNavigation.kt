package com.fedi.smartparking.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fedi.smartparking.R

private data class BottomDestination(
    val route: String,
    val labelRes: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

private val bottomDestinations = listOf(
    BottomDestination(NavRoutes.HOME, R.string.nav_home, Icons.Default.Home),
    BottomDestination(NavRoutes.EXPLORE, R.string.nav_explore, Icons.Default.Explore),
    BottomDestination(NavRoutes.BOOKINGS, R.string.nav_bookings, Icons.Default.CalendarMonth),
    BottomDestination(NavRoutes.PROFILE, R.string.nav_profile, Icons.Default.Person),
)

@Composable
fun DriverBottomNavigation(navController: NavHostController) {
    val currentRoute = navController
        .currentBackStackEntryAsState()
        .value
        ?.destination
        ?.route

    NavigationBar {
        bottomDestinations.forEach { destination ->
            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(NavRoutes.HOME) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(destination.labelRes),
                    )
                },
                label = { Text(text = stringResource(destination.labelRes)) },
            )
        }
    }
}
