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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fedi.smartparking.R
import com.fedi.smartparking.features.home.ParkingAvailabilityStore
import com.fedi.smartparking.features.home.ParkingCardUiModel
import com.fedi.smartparking.features.home.ParkingPhoto
import com.fedi.smartparking.ui.theme.LightBorder
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun ExploreScreen(
    onParkingClick: (ParkingCardUiModel) -> Unit = {},
    onBack: () -> Unit = {},
    exploreViewModel: ExploreViewModel = viewModel(),
) {
    val uiState by exploreViewModel.uiState.collectAsStateWithLifecycle()
    val visibleParkings = uiState.parkings
        .map { parking ->
            parking.copy(
                availableSpaces = ParkingAvailabilityStore.available(
                    parking.name,
                    parking.availableSpaces,
                ),
            )
        }
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
        item {
            ParkingMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
            )
        }
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

private data class ExploreMapLocation(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val price: Double,
)

private val exploreMapLocations = listOf(
    ExploreMapLocation("Parking Avenue", 36.8008, 10.1800, 2.5),
    ExploreMapLocation("Parking Lafayette", 36.8085, 10.1815, 2.0),
    ExploreMapLocation("Parking Municipal", 36.8065, 10.1810, 1.8),
    ExploreMapLocation("Parking Les Berges du Lac", 36.8310, 10.2390, 2.8),
    ExploreMapLocation("Parking Lac 2", 36.8460, 10.2600, 2.2),
    ExploreMapLocation("Parking El Menzah", 36.8420, 10.1650, 1.5),
    ExploreMapLocation("Parking Mutuelleville", 36.8330, 10.1780, 1.7),
    ExploreMapLocation("Parking La Marsa Centre", 36.8780, 10.3240, 3.0),
    ExploreMapLocation("Parking Carthage", 36.8520, 10.3230, 2.4),
    ExploreMapLocation("Parking Bab Bhar", 36.8010, 10.1810, 1.6),
)

@Composable
fun ParkingMap(modifier: Modifier = Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val mapView = remember {
        Configuration.getInstance().userAgentValue = context.packageName
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(12.0)
            controller.setCenter(GeoPoint(36.8240, 10.1900))
        }
    }

    DisposableEffect(mapView) {
        mapView.onResume()
        onDispose { mapView.onPause() }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier.clip(RoundedCornerShape(24.dp)),
        update = { view ->
            view.overlays.removeAll { it is Marker }
            exploreMapLocations.forEach { location ->
                view.overlays.add(
                    Marker(view).apply {
                        position = GeoPoint(location.latitude, location.longitude)
                        title = location.name
                        snippet = "${location.price} DT / heure"
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    },
                )
            }
            view.invalidate()
        },
    )
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
            ParkingPhoto(
                parking = parking,
                modifier = Modifier.size(92.dp),
            )
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
