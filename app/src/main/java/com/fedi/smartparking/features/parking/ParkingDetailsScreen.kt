package com.fedi.smartparking.features.parking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fedi.smartparking.R
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue

@Composable
fun ParkingDetailsScreen(
    parkingName: String,
    onBack: () -> Unit,
    onReserve: () -> Unit,
) {
    val parking = parkingDetails(parkingName)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFFBFDDF7), Color(0xFFEAF4FD)),
                    ),
                ),
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(14.dp)
                    .background(Color.White.copy(alpha = 0.9f), CircleShape),
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.explore_back),
                    tint = Navy,
                )
            }
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null,
                tint = PrimaryBlue.copy(alpha = 0.72f),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(100.dp),
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(18.dp),
                color = Color.White,
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    text = stringResource(R.string.parking_open_now),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    color = Color(0xFF168A68),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = parking.name,
                style = MaterialTheme.typography.headlineSmall,
                color = Navy,
                fontWeight = FontWeight.Bold,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(19.dp),
                )
                Text(
                    text = parking.address,
                    modifier = Modifier.padding(start = 6.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                DetailMetric(
                    value = stringResource(R.string.home_per_hour, parking.hourlyPrice),
                    label = stringResource(R.string.parking_price_label),
                    modifier = Modifier.weight(1f),
                )
                DetailMetric(
                    value = parking.availableSpaces.toString(),
                    label = stringResource(R.string.parking_spaces_label),
                    modifier = Modifier.weight(1f),
                )
                DetailMetric(
                    value = stringResource(R.string.home_distance, parking.distanceKm),
                    label = stringResource(R.string.parking_distance_label),
                    modifier = Modifier.weight(1f),
                )
            }
            Text(
                text = stringResource(R.string.parking_amenities),
                style = MaterialTheme.typography.titleLarge,
                color = Navy,
                fontWeight = FontWeight.Bold,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Amenity(
                    icon = Icons.Default.Security,
                    label = stringResource(R.string.parking_secure),
                    modifier = Modifier.weight(1f),
                )
                Amenity(
                    icon = Icons.Default.Wc,
                    label = stringResource(R.string.parking_accessible),
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onReserve,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
            ) {
                Text(
                    text = stringResource(R.string.parking_reserve),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun DetailMetric(value: String, label: String, modifier: Modifier) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Text(value, color = Navy, fontWeight = FontWeight.Bold)
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun Amenity(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    modifier: Modifier,
) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryBlue)
            Text(label, modifier = Modifier.padding(start = 8.dp), color = Navy)
        }
    }
}

private data class ParkingDetails(
    val name: String,
    val address: String,
    val hourlyPrice: Double,
    val availableSpaces: Int,
    val distanceKm: Double,
)

private fun parkingDetails(name: String): ParkingDetails =
    when (name) {
        "Parking Lafayette" -> ParkingDetails(name, "Rue de Palestine", 2.0, 8, 0.8)
        "Parking Municipal" -> ParkingDetails(name, "Centre-ville de Tunis", 1.8, 5, 1.2)
        else -> ParkingDetails(name, "Avenue Habib Bourguiba", 2.5, 12, 0.3)
    }
