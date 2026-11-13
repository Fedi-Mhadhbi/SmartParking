package com.fedi.smartparking.features.explore

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.fedi.smartparking.features.home.ParkingCardUiModel
import com.fedi.smartparking.ui.theme.LightBorder
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue

@Composable
fun ExploreScreen(
    onParkingClick: (ParkingCardUiModel) -> Unit = {},
    onBack: () -> Unit = {},
    exploreViewModel: ExploreViewModel = viewModel(),
) {
    val uiState by exploreViewModel.uiState.collectAsStateWithLifecycle()
    val visibleParkings = uiState.parkings
        .filter { parking ->
            uiState.query.isBlank() ||
                parking.name.contains(uiState.query, ignoreCase = true) ||
                parking.address.contains(uiState.query, ignoreCase = true)
        }
        .let { parkings ->
            when (uiState.selectedFilter) {
                ExploreFilter.NEARBY -> parkings.sortedBy { it.distanceKm }
                ExploreFilter.LOWEST_PRICE -> parkings.sortedBy { it.hourlyPrice }
                ExploreFilter.AVAILABLE -> parkings.sortedByDescending { it.availableSpaces }
            }
        }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 20.dp,
            top = 14.dp,
            end = 20.dp,
            bottom = 28.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.explore_back),
                        tint = Navy,
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.explore_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Navy,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stringResource(R.string.explore_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Surface(
                    color = Color.White,
                    shape = CircleShape,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder),
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.padding(10.dp).size(20.dp),
                    )
                }
            }
        }
        item {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = exploreViewModel::updateQuery,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text(stringResource(R.string.explore_search_hint)) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    Icon(Icons.Default.Tune, contentDescription = null)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = LightBorder,
                ),
            )
        }
        item { ExploreMapPreview() }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ExploreFilterChip(
                    text = stringResource(R.string.explore_filter_nearby),
                    selected = uiState.selectedFilter == ExploreFilter.NEARBY,
                    onClick = { exploreViewModel.selectFilter(ExploreFilter.NEARBY) },
                )
                ExploreFilterChip(
                    text = stringResource(R.string.explore_filter_price),
                    selected = uiState.selectedFilter == ExploreFilter.LOWEST_PRICE,
                    onClick = { exploreViewModel.selectFilter(ExploreFilter.LOWEST_PRICE) },
                )
                ExploreFilterChip(
                    text = stringResource(R.string.explore_filter_available),
                    selected = uiState.selectedFilter == ExploreFilter.AVAILABLE,
                    onClick = { exploreViewModel.selectFilter(ExploreFilter.AVAILABLE) },
                )
            }
        }
        item {
            Text(
                text = stringResource(R.string.explore_results),
                style = MaterialTheme.typography.titleLarge,
                color = Navy,
                fontWeight = FontWeight.Bold,
            )
        }
        items(visibleParkings) { parking ->
            ExploreParkingCard(parking = parking, onClick = { onParkingClick(parking) })
        }
        if (visibleParkings.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.explore_empty),
                    modifier = Modifier.padding(vertical = 28.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun ExploreMapPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(listOf(Color(0xFFDCEBFA), Color(0xFFF6F9FC))),
            ),
    ) {
        repeat(5) { index ->
            Box(
                modifier = Modifier
                    .align(if (index % 2 == 0) Alignment.TopStart else Alignment.BottomEnd)
                    .padding(
                        start = (20 + index * 34).dp,
                        top = (28 + index * 18).dp,
                    )
                    .width(130.dp)
                    .height(2.dp)
                    .background(Color.White.copy(alpha = 0.9f)),
            )
        }
        ExploreMapPin(Modifier.align(Alignment.Center), "2.5 DT")
        ExploreMapPin(Modifier.align(Alignment.TopEnd).padding(top = 42.dp, end = 42.dp), "1.8 DT")
        ExploreMapPin(Modifier.align(Alignment.BottomStart).padding(bottom = 26.dp, start = 54.dp), "2 DT")
        Text(
            text = stringResource(R.string.explore_map_label),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(14.dp)
                .background(Color.White.copy(alpha = 0.92f), RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 7.dp),
            style = MaterialTheme.typography.labelLarge,
            color = Navy,
        )
    }
}

@Composable
private fun ExploreMapPin(modifier: Modifier, price: String) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(color = PrimaryBlue, shape = CircleShape, shadowElevation = 4.dp) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(8.dp).size(18.dp),
            )
        }
        Text(
            text = price,
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
private fun ExploreFilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = PrimaryBlue,
            selectedLabelColor = Color.White,
        ),
    )
}

@Composable
private fun ExploreParkingCard(parking: ParkingCardUiModel, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(78.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(parking.accentColor).copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(parking.accentColor),
                    modifier = Modifier.size(34.dp),
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(parking.name, style = MaterialTheme.typography.titleMedium, color = Navy)
                Text(parking.address, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = stringResource(R.string.explore_spaces, parking.availableSpaces),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF168A68),
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(R.string.home_per_hour, parking.hourlyPrice),
                    style = MaterialTheme.typography.titleMedium,
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.home_distance, parking.distanceKm),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
