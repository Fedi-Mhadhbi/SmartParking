package com.fedi.smartparking.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fedi.smartparking.R
import com.fedi.smartparking.ui.theme.AppTheme
import com.fedi.smartparking.ui.theme.LightBorder
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue
import com.fedi.smartparking.ui.theme.SmartParkingSpacing

@Composable
fun DriverHomeScreen(
    onParkingClick: (ParkingCardUiModel) -> Unit,
    homeViewModel: HomeViewModel = viewModel(),
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = SmartParkingSpacing.screenHorizontal),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            HomeHeader(
                userName = uiState.userName,
                location = uiState.location,
            )
            SearchField(
                query = uiState.searchQuery,
                onQueryChange = homeViewModel::updateSearchQuery,
            )
            MapPreview()
            SectionHeader(
                title = stringResource(R.string.home_nearby_title),
                action = stringResource(R.string.home_see_all),
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                items(uiState.nearbyParkings) { parking ->
                    ParkingCard(
                        parking = parking,
                        onClick = { onParkingClick(parking) },
                    )
                }
            }
            SectionHeader(
                title = stringResource(R.string.home_recommended),
                action = stringResource(R.string.home_see_all),
            )
            FeaturedParkingCard(
                parking = uiState.nearbyParkings.first(),
                onClick = { onParkingClick(uiState.nearbyParkings.first()) },
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun HomeHeader(
    userName: String,
    location: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.home_greeting, userName),
                style = MaterialTheme.typography.headlineSmall,
                color = Navy,
            )
            Text(
                text = stringResource(R.string.home_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        IconButton(
            onClick = {},
            modifier = Modifier
                .size(46.dp)
                .background(Color.White, CircleShape)
                .border(1.dp, LightBorder, CircleShape),
        ) {
            Icon(
                imageVector = Icons.Default.NotificationsNone,
                contentDescription = null,
                tint = Navy,
            )
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        placeholder = {
            Text(text = stringResource(R.string.home_search_hint))
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },
        trailingIcon = {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Default.Tune, contentDescription = null)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = LightBorder,
        ),
    )
}

@Composable
private fun MapPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(178.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFDCEBFA), Color(0xFFF4F7FB)),
                ),
            )
            .border(1.dp, LightBorder, RoundedCornerShape(22.dp)),
    ) {
        Text(
            text = stringResource(R.string.home_map_preview),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.92f), RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 7.dp),
            style = MaterialTheme.typography.labelLarge,
            color = Navy,
        )
        MapPin(
            modifier = Modifier.align(Alignment.Center),
            price = 2.5,
        )
        MapPin(
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 54.dp, end = 38.dp),
            price = 1.8,
        )
        MapPin(
            modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 26.dp, start = 46.dp),
            price = 2.0,
        )
    }
}

@Composable
private fun MapPin(
    modifier: Modifier,
    price: Double,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            color = PrimaryBlue,
            shape = CircleShape,
            shadowElevation = 4.dp,
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(8.dp).size(18.dp),
            )
        }
        Text(
            text = stringResource(R.string.home_per_hour, price),
            modifier = Modifier
                .padding(top = 2.dp)
                .background(Color.White, RoundedCornerShape(6.dp))
                .padding(horizontal = 5.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Navy,
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    action: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = Navy,
        )
        Text(
            text = action,
            style = MaterialTheme.typography.labelLarge,
            color = PrimaryBlue,
        )
    }
}

@Composable
private fun ParkingCard(
    parking: ParkingCardUiModel,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .width(258.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column {
            ParkingIllustration(
                accentColor = Color(parking.accentColor),
                modifier = Modifier.fillMaxWidth().height(116.dp),
            )
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp),
            ) {
                Text(
                    text = parking.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Navy,
                )
                Text(
                    text = parking.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.home_distance, parking.distanceKm),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = stringResource(R.string.home_per_hour, parking.hourlyPrice),
                        style = MaterialTheme.typography.labelLarge,
                        color = PrimaryBlue,
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedParkingCard(
    parking: ParkingCardUiModel,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ParkingIllustration(
                accentColor = Color(parking.accentColor),
                modifier = Modifier.size(88.dp).clip(RoundedCornerShape(14.dp)),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text(
                    text = parking.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Navy,
                )
                Text(
                    text = stringResource(R.string.home_available_spaces, parking.availableSpaces),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.colors.available,
                )
                Text(
                    text = stringResource(R.string.home_open_now),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.colors.available,
                    fontWeight = FontWeight.Medium,
                )
            }
            Text(
                text = stringResource(R.string.home_per_hour, parking.hourlyPrice),
                style = MaterialTheme.typography.labelLarge,
                color = PrimaryBlue,
            )
        }
    }
}

@Composable
private fun ParkingIllustration(
    accentColor: Color,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    listOf(accentColor.copy(alpha = 0.9f), Navy),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "P",
            color = Color.White,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
        )
    }
}
