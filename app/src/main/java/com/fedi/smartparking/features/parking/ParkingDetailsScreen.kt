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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LocalParking
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
import com.fedi.smartparking.features.home.ParkingAvailabilityStore
import com.fedi.smartparking.features.home.ParkingCardUiModel
import com.fedi.smartparking.features.home.ParkingPhoto
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
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
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
            ParkingPhoto(
                parking = ParkingCardUiModel(
                    name = parking.name,
                    address = parking.address,
                    distanceKm = parking.distanceKm,
                    hourlyPrice = parking.hourlyPrice,
                    availableSpaces = parking.availableSpaces,
                    accentColor = 0xFF034EE1,
                    imageResId = parking.imageResId,
                ),
                modifier = Modifier.fillMaxSize(),
            )
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
                    value = ParkingAvailabilityStore.available(
                        parking.name,
                        parking.availableSpaces,
                    ).toString(),
                    label = stringResource(R.string.parking_spaces_label),
                    modifier = Modifier.weight(1f),
                )
                DetailMetric(
                    value = stringResource(R.string.home_distance, parking.distanceKm),
                    label = stringResource(R.string.parking_distance_label),
                    modifier = Modifier.weight(1f),
                )
            }
            val availableSpaces = ParkingAvailabilityStore.available(
                parking.name,
                parking.availableSpaces,
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = if (availableSpaces > 0) Color(0xFFE9F8F1) else Color(0xFFFFEDEC),
                shape = RoundedCornerShape(16.dp),
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalParking,
                        contentDescription = null,
                        tint = if (availableSpaces > 0) Color(0xFF168A68) else Color(0xFFB3261E),
                        modifier = Modifier.size(24.dp),
                    )
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Text(
                            text = if (availableSpaces > 0) {
                                stringResource(R.string.parking_available_now)
                            } else {
                                stringResource(R.string.parking_full)
                            },
                            color = if (availableSpaces > 0) Color(0xFF126B52) else Color(0xFF8C1D18),
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = stringResource(
                                R.string.parking_available_spaces,
                                availableSpaces,
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
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
            Spacer(modifier = Modifier.height(4.dp))
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
    val imageResId: Int,
)

private fun parkingDetails(name: String): ParkingDetails =
    when (name) {
        "Parking Lafayette" -> ParkingDetails(name, "Rue de Palestine", 2.0, 8, 0.8, R.drawable.lafayette)
        "Parking Municipal" -> ParkingDetails(name, "Centre-ville de Tunis", 1.8, 5, 1.2, R.drawable.bab_bhar)
        "Parking Les Berges du Lac" -> ParkingDetails(name, "Les Berges du Lac 1", 2.8, 21, 2.4, R.drawable.lac_2)
        "Parking Lac 2" -> ParkingDetails(name, "Les Berges du Lac 2", 2.2, 14, 3.1, R.drawable.lac_2)
        "Parking El Menzah" -> ParkingDetails(name, "El Menzah 5", 1.5, 9, 3.8, R.drawable.el_menzah)
        "Parking Mutuelleville" -> ParkingDetails(name, "Mutuelleville, Tunis", 1.7, 17, 4.2, R.drawable.el_menzah)
        "Parking La Marsa Centre" -> ParkingDetails(name, "Avenue Habib Bourguiba, La Marsa", 3.0, 26, 11.6, R.drawable.la_marsa)
        "Parking Carthage" -> ParkingDetails(name, "Carthage Hannibal", 2.4, 11, 10.4, R.drawable.carthage)
        "Parking Bab Bhar" -> ParkingDetails(name, "Place de la Victoire", 1.6, 7, 0.9, R.drawable.bab_bhar)
        else -> ParkingDetails(name, "Avenue Habib Bourguiba", 2.5, 12, 0.3, R.drawable.tunis_theatre)
    }
