package com.fedi.smartparking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.fedi.smartparking.navigation.AppNavigation
import com.fedi.smartparking.features.profile.applySavedLanguage
import com.fedi.smartparking.ui.theme.SmartParkingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applySavedLanguage(this)
        setContent {
            SmartParkingTheme {
                AppNavigation()
            }
        }
    }
}
