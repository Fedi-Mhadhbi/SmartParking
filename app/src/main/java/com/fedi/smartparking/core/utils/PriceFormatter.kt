package com.fedi.smartparking.core.utils

import java.util.Locale

const val DISPLAY_CURRENCY_LABEL = "DT"

fun formatPrice(
    amount: Double,
    suffix: String = "",
): String {
    val formattedAmount = String.format(Locale.US, "%.3f", amount)
    return if (suffix.isBlank()) {
        "$formattedAmount $DISPLAY_CURRENCY_LABEL"
    } else {
        "$formattedAmount $DISPLAY_CURRENCY_LABEL $suffix"
    }
}
