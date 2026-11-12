package com.fedi.smartparking.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fedi.smartparking.R
import com.fedi.smartparking.ui.components.SmartButton
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue
import com.fedi.smartparking.ui.theme.SmartParkingSpacing

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    onSignIn: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SmartParkingSpacing.screenHorizontal),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.linearGradient(
                        listOf(PrimaryBlue, Navy),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "P",
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge,
                )
                Text(
                    text = stringResource(R.string.auth_app_tagline),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
        Column(
            modifier = Modifier.padding(vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.auth_welcome_title),
                style = MaterialTheme.typography.displaySmall,
                color = Navy,
            )
            Text(
                text = stringResource(R.string.auth_welcome_body),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Column(
            modifier = Modifier.padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SmartButton(
                text = stringResource(R.string.auth_get_started),
                onClick = onGetStarted,
            )
            androidx.compose.foundation.layout.Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onSignIn)
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.auth_have_account),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = " ${stringResource(R.string.auth_sign_in)}",
                    color = PrimaryBlue,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}
