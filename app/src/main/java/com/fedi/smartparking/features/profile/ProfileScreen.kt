package com.fedi.smartparking.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.fedi.smartparking.R
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue

@Composable
fun ProfileScreen(
    onHelpClick: () -> Unit,
    onBookingsClick: () -> Unit,
    onVehiclesClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onLogout: () -> Unit,
) {
    val user = FirebaseAuth.getInstance().currentUser
    val displayName = user?.displayName?.takeIf { it.isNotBlank() }
        ?: user?.email?.substringBefore("@")
        ?: stringResource(R.string.profile_guest)
    val email = user?.email.orEmpty()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Text(
            text = stringResource(R.string.profile_title),
            style = MaterialTheme.typography.headlineSmall,
            color = Navy,
            fontWeight = FontWeight.Bold,
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Navy,
            shape = RoundedCornerShape(22.dp),
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(color = PrimaryBlue, shape = CircleShape) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(12.dp).size(28.dp),
                    )
                }
                Column(modifier = Modifier.padding(start = 14.dp)) {
                    Text(
                        text = displayName,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = email,
                        color = Color.White.copy(alpha = 0.75f),
                    )
                }
            }
        }
        ProfileOption(
            icon = Icons.Default.CalendarMonth,
            title = stringResource(R.string.profile_bookings),
            subtitle = stringResource(R.string.profile_bookings_subtitle),
            onClick = onBookingsClick,
        )
        ProfileOption(
            icon = Icons.Default.DirectionsCar,
            title = stringResource(R.string.profile_vehicles),
            subtitle = stringResource(R.string.profile_vehicles_subtitle),
            onClick = onVehiclesClick,
        )
        ProfileOption(
            icon = Icons.Default.NotificationsNone,
            title = stringResource(R.string.profile_notifications),
            subtitle = stringResource(R.string.profile_notifications_subtitle),
            onClick = onNotificationsClick,
        )
        ProfileOption(
            icon = Icons.Default.Language,
            title = stringResource(R.string.profile_language),
            subtitle = stringResource(R.string.profile_language_subtitle),
            onClick = onLanguageClick,
        )
        ProfileOption(
            icon = Icons.Default.HelpOutline,
            title = stringResource(R.string.profile_help),
            subtitle = stringResource(R.string.profile_help_subtitle),
            onClick = onHelpClick,
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            onClick = onLogout,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFB42318),
                contentColor = Color.White,
            ),
        ) {
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp),
                )
                Text(
                    text = stringResource(R.string.profile_logout),
                    modifier = Modifier.padding(start = 14.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun ProfileOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(28.dp))
            Column(modifier = Modifier.weight(1f).padding(start = 14.dp)) {
                Text(title, color = Navy, fontWeight = FontWeight.Bold)
                Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Navy)
        }
    }
}
