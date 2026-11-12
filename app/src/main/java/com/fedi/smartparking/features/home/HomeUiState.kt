package com.fedi.smartparking.features.home

data class HomeUiState(
    val userName: String = "Ali",
    val location: String = "Tunis, Tunisia",
    val searchQuery: String = "",
    val nearbyParkings: List<ParkingCardUiModel> = sampleParkings,
)

data class ParkingCardUiModel(
    val name: String,
    val address: String,
    val distanceKm: Double,
    val hourlyPrice: Double,
    val availableSpaces: Int,
    val accentColor: Long,
)

private val sampleParkings = listOf(
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
