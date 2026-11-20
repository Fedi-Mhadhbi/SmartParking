package com.fedi.smartparking.features.explore

import com.fedi.smartparking.R
import androidx.lifecycle.ViewModel
import com.fedi.smartparking.features.home.ParkingCardUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ExploreUiState(
    val query: String = "",
    val selectedFilter: ExploreFilter = ExploreFilter.NEARBY,
    val parkings: List<ParkingCardUiModel> = exploreParkings,
)

enum class ExploreFilter {
    NEARBY,
    LOWEST_PRICE,
    AVAILABLE,
}

class ExploreViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    fun updateQuery(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    fun selectFilter(filter: ExploreFilter) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }
}

private val exploreParkings = listOf(
    ParkingCardUiModel(
        name = "Parking Avenue",
        address = "Avenue Habib Bourguiba",
        distanceKm = 0.3,
        hourlyPrice = 2.5,
        availableSpaces = 12,
        accentColor = 0xFF034EE1,
        imageResId = R.drawable.tunis_theatre,
    ),
    ParkingCardUiModel(
        name = "Parking Lafayette",
        address = "Rue de Palestine",
        distanceKm = 0.8,
        hourlyPrice = 2.0,
        availableSpaces = 8,
        accentColor = 0xFF16A67A,
        imageResId = R.drawable.lafayette,
    ),
    ParkingCardUiModel(
        name = "Parking Municipal",
        address = "Centre-ville de Tunis",
        distanceKm = 1.2,
        hourlyPrice = 1.8,
        availableSpaces = 5,
        accentColor = 0xFFF59E0B,
        imageResId = R.drawable.bab_bhar,
    ),
    ParkingCardUiModel(
        name = "Parking Les Berges du Lac",
        address = "Les Berges du Lac 1",
        distanceKm = 2.4,
        hourlyPrice = 2.8,
        availableSpaces = 21,
        accentColor = 0xFF7C3AED,
        imageResId = R.drawable.lac_2,
    ),
    ParkingCardUiModel(
        name = "Parking Lac 2",
        address = "Les Berges du Lac 2",
        distanceKm = 3.1,
        hourlyPrice = 2.2,
        availableSpaces = 14,
        accentColor = 0xFF0891B2,
        imageResId = R.drawable.lac_2,
    ),
    ParkingCardUiModel(
        name = "Parking El Menzah",
        address = "El Menzah 5",
        distanceKm = 3.8,
        hourlyPrice = 1.5,
        availableSpaces = 9,
        accentColor = 0xFFDC2626,
        imageResId = R.drawable.el_menzah,
    ),
    ParkingCardUiModel(
        name = "Parking Mutuelleville",
        address = "Mutuelleville, Tunis",
        distanceKm = 4.2,
        hourlyPrice = 1.7,
        availableSpaces = 17,
        accentColor = 0xFFCA8A04,
        imageResId = R.drawable.el_menzah,
    ),
    ParkingCardUiModel(
        name = "Parking La Marsa Centre",
        address = "Avenue Habib Bourguiba, La Marsa",
        distanceKm = 11.6,
        hourlyPrice = 3.0,
        availableSpaces = 26,
        accentColor = 0xFF0F766E,
        imageResId = R.drawable.la_marsa,
    ),
    ParkingCardUiModel(
        name = "Parking Carthage",
        address = "Carthage Hannibal",
        distanceKm = 10.4,
        hourlyPrice = 2.4,
        availableSpaces = 11,
        accentColor = 0xFF9333EA,
        imageResId = R.drawable.carthage,
    ),
    ParkingCardUiModel(
        name = "Parking Bab Bhar",
        address = "Place de la Victoire",
        distanceKm = 0.9,
        hourlyPrice = 1.6,
        availableSpaces = 7,
        accentColor = 0xFFEA580C,
        imageResId = R.drawable.bab_bhar,
    ),
)
