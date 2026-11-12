package com.fedi.smartparking.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = White,
    secondary = Navy,
    onSecondary = White,
    background = Background,
    onBackground = Navy,
    surface = White,
    onSurface = Navy,
    onSurfaceVariant = NeutralGrey,
    error = Red,
    onError = White,
    outline = LightBorder,
    outlineVariant = LightBorder,
)

val LocalSmartParkingColors = staticCompositionLocalOf { SmartParkingColors() }

object AppTheme {
    val colors: SmartParkingColors
        @Composable get() = LocalSmartParkingColors.current
}

@Composable
@Suppress("UNUSED_PARAMETER")
fun SmartParkingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalSmartParkingColors provides SmartParkingColors()) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = SmartParkingTypography,
            shapes = SmartParkingShapes,
            content = content,
        )
    }
}
