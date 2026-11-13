package com.fedi.smartparking.navigation

object NavRoutes {
    const val HOME = "home"
    const val EXPLORE = "explore"
    const val BOOKINGS = "bookings"
    const val PROFILE = "profile"
    const val PARKING_DETAILS = "parking_details/{parkingName}"

    fun parkingDetails(parkingName: String): String =
        "parking_details/${android.net.Uri.encode(parkingName)}"
}
