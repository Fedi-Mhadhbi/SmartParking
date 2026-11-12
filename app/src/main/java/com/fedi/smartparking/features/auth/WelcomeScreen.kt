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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
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
import androidx.compose.ui.unit.dp
import com.fedi.smartparking.R
import com.fedi.smartparking.ui.components.SmartButton
import com.fedi.smartparking.ui.theme.Emerald
import com.fedi.smartparking.ui.theme.LightBorder
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue
import com.fedi.smartparking.ui.theme.SmartParkingSpacing
import com.fedi.smartparking.ui.theme.White

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    onSignIn: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF9FCFF), Color(0xFFEAF4FF)),
                ),
            )
            .padding(horizontal = SmartParkingSpacing.screenHorizontal),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(28.dp))
        EasyParLogo()
        Spacer(modifier = Modifier.height(24.dp))
        WelcomeIllustration()
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.auth_welcome_title),
            style = MaterialTheme.typography.headlineSmall,
            color = Navy,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.auth_welcome_body),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge,
            color = Navy.copy(alpha = 0.72f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(18.dp))
        OnboardingIndicators()
        Spacer(modifier = Modifier.weight(1f))
        SmartButton(
            text = stringResource(R.string.auth_get_started),
            onClick = onGetStarted,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onSignIn)
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.auth_have_account),
                color = Navy.copy(alpha = 0.72f),
            )
            Text(
                text = " ${stringResource(R.string.auth_sign_in)}",
                modifier = Modifier
                    .padding(start = 4.dp)
                    .then(
                        Modifier,
                    ),
                color = Emerald,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        }
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
        Box(
            modifier = Modifier
                .size(if (compact) 58.dp else 78.dp)
                .shadow(8.dp, CircleShape)
                .background(
                    Brush.linearGradient(listOf(Emerald, PrimaryBlue)),
                    CircleShape,
                )
                .padding(7.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Navy, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "P",
                    color = White,
                    style = if (compact) {
                        MaterialTheme.typography.headlineSmall
                    } else {
                        MaterialTheme.typography.displaySmall
                    },
                    fontWeight = FontWeight.Bold,
                )
                IconCarBadge(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(if (compact) 0.dp else 2.dp),
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Easy",
                color = Navy,
                style = if (compact) {
                    MaterialTheme.typography.titleMedium
                } else {
                    MaterialTheme.typography.headlineSmall
                },
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Par",
                color = Emerald,
                style = if (compact) {
                    MaterialTheme.typography.titleMedium
                } else {
                    MaterialTheme.typography.headlineSmall
                },
                fontWeight = FontWeight.Bold,
            )
        }
        if (!compact) {
            Text(
                text = stringResource(R.string.auth_app_tagline),
                color = Navy,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun IconCarBadge(modifier: Modifier) {
    Surface(
        modifier = modifier.size(25.dp),
        color = White,
        shape = CircleShape,
        shadowElevation = 2.dp,
    ) {
        androidx.compose.material3.Icon(
            imageVector = Icons.Default.DirectionsCar,
            contentDescription = null,
            tint = Emerald,
            modifier = Modifier.padding(4.dp),
        )
    }
}

@Composable
private fun WelcomeIllustration() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFDCEEFF), Color(0xFFF7FBFF)),
                ),
            )
            .then(Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(104.dp)
                    .background(
                        Brush.linearGradient(listOf(Emerald, PrimaryBlue)),
                        RoundedCornerShape(52.dp, 52.dp, 52.dp, 8.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "P",
                    color = White,
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(72.dp),
            )
            Text(
                text = stringResource(R.string.auth_welcome_illustration),
                color = Navy.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun OnboardingIndicators() {
    Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == 0) 9.dp else 7.dp)
                    .background(
                        if (index == 0) PrimaryBlue else LightBorder,
                        CircleShape,
                    ),
            )
        }
    }
}
