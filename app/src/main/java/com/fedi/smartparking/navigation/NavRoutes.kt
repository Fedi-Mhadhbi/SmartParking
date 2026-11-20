package com.fedi.smartparking.navigation

object NavRoutes {
    const val HOME = "home"
    const val EXPLORE = "explore"
    const val BOOKINGS = "bookings"
    const val PROFILE = "profile"
    const val MY_VEHICLES = "my_vehicles"
    const val PARKING_DETAILS = "parking_details/{parkingName}"
    const val RESERVATION = "reservation/{parkingName}"
    const val BOOKING_CONFIRMATION = "booking_confirmation/{parkingName}"
    const val BOOKING_SUCCESS = "booking_success/{parkingName}"
    const val PAYMENT = "payment/{parkingName}"
    const val BOOKING_DETAILS = "booking_details/{parkingName}"
    const val NOTIFICATIONS = "notifications"
    const val HELP = "help"
    const val LANGUAGE = "language"

    fun parkingDetails(parkingName: String): String =
        "parking_details/${android.net.Uri.encode(parkingName)}"

    fun reservation(parkingName: String): String =
        "reservation/${android.net.Uri.encode(parkingName)}"

    fun bookingConfirmation(parkingName: String): String =
        "booking_confirmation/${android.net.Uri.encode(parkingName)}"

    fun bookingSuccess(parkingName: String): String =
        "booking_success/${android.net.Uri.encode(parkingName)}"

    fun payment(parkingName: String): String =
        "payment/${android.net.Uri.encode(parkingName)}"

    fun bookingDetails(parkingName: String): String =
        "booking_details/${android.net.Uri.encode(parkingName)}"
}
