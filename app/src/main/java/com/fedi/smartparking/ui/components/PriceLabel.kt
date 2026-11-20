package com.fedi.smartparking.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.fedi.smartparking.core.utils.formatPrice

@Composable
fun PriceLabel(
    price: Double,
    suffix: String = "/ hour",
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Text(
        text = formatPrice(price, suffix),
        color = color,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
    )
}
