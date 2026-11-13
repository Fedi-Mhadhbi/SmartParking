package com.fedi.smartparking.features.explore

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
    ),
    ParkingCardUiModel(
        name = "Parking Lafayette",
        address = "Rue de Palestine",
        distanceKm = 0.8,
        hourlyPrice = 2.0,
        availableSpaces = 8,
        accentColor = 0xFF16A67A,
    ),
    ParkingCardUiModel(
        name = "Parking Municipal",
        address = "Centre-ville de Tunis",
        distanceKm = 1.2,
        hourlyPrice = 1.8,
        availableSpaces = 5,
        accentColor = 0xFFF59E0B,
    ),
)
