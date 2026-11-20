package com.fedi.smartparking.ui.theme

import androidx.compose.ui.graphics.Color

val Navy = Color(0xFF142B45)
val PrimaryBlue = Color(0xFF034EE1)
val Emerald = Color(0xFF16A67A)
val Background = Color(0xFFF4F6F8)
val Amber = Color(0xFFF59E0B)
val Red = Color(0xFFDC2626)
val White = Color(0xFFFFFFFF)
val NeutralGrey = Color(0xFF9CA3AF)
val LightBorder = Color(0xFFE5E7EB)

data class SmartParkingColors(
    val available: Color = Emerald,
    val reserved: Color = Amber,
    val occupied: Color = NeutralGrey,
    val error: Color = Red,
)
