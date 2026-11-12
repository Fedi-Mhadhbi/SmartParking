package com.fedi.smartparking.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fedi.smartparking.R
import com.fedi.smartparking.ui.components.SmartButton
import com.fedi.smartparking.ui.theme.Emerald
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue
import com.fedi.smartparking.ui.theme.White

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    onSignIn: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFEAF5FF),
                        Color(0xFFFFFFFF),
                        Color(0xFFE9F5FF),
                    ),
                ),
            ),
    ) {
        SkyDecorations()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 34.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            EasyParLogo()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.auth_app_tagline),
                color = Navy,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(18.dp))
            WelcomeCityScene()
            Spacer(modifier = Modifier.weight(1f))
            WelcomeActionPanel(
                onGetStarted = onGetStarted,
                onSignIn = onSignIn,
            )
        }
    }
}

@Composable
private fun SkyDecorations() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = (-120).dp, y = (-80).dp)
                .background(
                    Color(0xFFDCEEFF).copy(alpha = 0.65f),
                    CircleShape,
                ),
        )
        Cloud(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 42.dp, y = 124.dp),
        )
        Cloud(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-34).dp, y = 270.dp),
        )
    }
}

@Composable
private fun Cloud(modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(Color(0xFFD5EAFE), CircleShape),
        )
        Box(
            modifier = Modifier
                .size(48.dp)
                .offset(x = (-8).dp)
                .background(Color(0xFFD5EAFE), CircleShape),
        )
        Box(
            modifier = Modifier
                .width(72.dp)
                .height(22.dp)
                .offset(x = (-18).dp)
                .background(Color(0xFFD5EAFE), RoundedCornerShape(14.dp)),
        )
    }
}

@Composable
fun EasyParLogo(
    compact: Boolean = false,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 10.dp),
    ) {
        ParkingPinLogo(compact = compact)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Easy",
                color = Navy,
                style = if (compact) {
                    MaterialTheme.typography.titleMedium
                } else {
                    MaterialTheme.typography.displaySmall
                },
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Par",
                color = Emerald,
                style = if (compact) {
                    MaterialTheme.typography.titleMedium
                } else {
                    MaterialTheme.typography.displaySmall
                },
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun ParkingPinLogo(compact: Boolean) {
    Box(
        modifier = Modifier
            .size(if (compact) 58.dp else 96.dp)
            .shadow(8.dp, RoundedCornerShape(50.dp, 50.dp, 50.dp, 8.dp))
            .background(
                Brush.linearGradient(listOf(Emerald, Color(0xFF08B98A))),
                RoundedCornerShape(50.dp, 50.dp, 50.dp, 8.dp),
            )
            .padding(if (compact) 6.dp else 9.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Navy, RoundedCornerShape(50.dp, 50.dp, 50.dp, 8.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "P",
                color = White,
                style = if (compact) {
                    MaterialTheme.typography.headlineSmall
                } else {
                    MaterialTheme.typography.displayLarge
                },
                fontWeight = FontWeight.Bold,
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(if (compact) 22.dp else 34.dp),
                color = White,
                shape = CircleShape,
                shadowElevation = 3.dp,
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = Emerald,
                    modifier = Modifier.padding(if (compact) 4.dp else 6.dp),
                )
            }
        }
    }
}

@Composable
private fun WelcomeCityScene() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(310.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        CitySkyline()
        ParkingSign(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 34.dp, bottom = 54.dp),
        )
        BlueCar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
        )
    }
}

@Composable
private fun CitySkyline() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom,
    ) {
        repeat(9) { index ->
            val height = 72 + (index % 4) * 23
            Box(
                modifier = Modifier
                    .width(if (index % 3 == 0) 28.dp else 36.dp)
                    .height(height.dp)
                    .background(
                        Color(0xFFBBDCF9).copy(alpha = 0.62f),
                        RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                    ),
            )
        }
    }
}

@Composable
private fun ParkingSign(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            modifier = Modifier.size(66.dp),
            color = PrimaryBlue,
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 5.dp,
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.LocalParking,
                contentDescription = null,
                tint = White,
                modifier = Modifier.padding(12.dp),
            )
        }
        Box(
            modifier = Modifier
                .width(5.dp)
                .height(58.dp)
                .background(Navy),
        )
    }
}

@Composable
private fun BlueCar(modifier: Modifier) {
    Box(
        modifier = modifier
            .width(250.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(80.dp, 80.dp, 30.dp, 30.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF2C9BFF), PrimaryBlue),
                ),
            )
            .shadow(8.dp, RoundedCornerShape(80.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .width(130.dp)
                .height(38.dp)
                .offset(y = (-28).dp)
                .clip(RoundedCornerShape(30.dp, 30.dp, 10.dp, 10.dp))
                .background(Color(0xFF12385C)),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .align(Alignment.BottomCenter)
                .offset(y = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            CarWheel()
            CarWheel()
        }
    }
}

@Composable
private fun CarWheel() {
    Box(
        modifier = Modifier
            .size(34.dp)
            .background(Navy, CircleShape)
            .padding(7.dp)
            .background(Color(0xFFB9C9D8), CircleShape),
    )
}

@Composable
private fun WelcomeActionPanel(
    onGetStarted: () -> Unit,
    onSignIn: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 42.dp, topEnd = 42.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Navy, Color(0xFF0B2540)),
                ),
            )
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SmartButton(
            text = stringResource(R.string.auth_get_started),
            onClick = onGetStarted,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onSignIn)
                .padding(top = 16.dp, bottom = 2.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.auth_have_account),
                color = White.copy(alpha = 0.78f),
            )
            Text(
                text = " ${stringResource(R.string.auth_sign_in)}",
                color = Color(0xFF62E0B9),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
