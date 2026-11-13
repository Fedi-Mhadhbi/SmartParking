package com.fedi.smartparking.features.reservation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fedi.smartparking.R
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue

@Composable
fun BookingConfirmationScreen(
    parkingName: String,
    onBack: () -> Unit,
    onConfirm: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.explore_back),
                    tint = Navy,
                )
            }
            Text(
                text = stringResource(R.string.confirmation_title),
                style = MaterialTheme.typography.headlineSmall,
                color = Navy,
                fontWeight = FontWeight.Bold,
            )
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFE9F8F3),
            shape = RoundedCornerShape(20.dp),
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF168A68),
                )
                Text(
                    text = stringResource(R.string.confirmation_ready),
                    style = MaterialTheme.typography.titleLarge,
                    color = Navy,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.confirmation_review),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        ConfirmationRow(stringResource(R.string.confirmation_parking), parkingName)
        ConfirmationRow(stringResource(R.string.confirmation_date), stringResource(R.string.reservation_select_date))
        ConfirmationRow(stringResource(R.string.confirmation_duration), stringResource(R.string.reservation_hours, 1))
        ConfirmationRow(stringResource(R.string.confirmation_total), stringResource(R.string.home_per_hour, 2.5))
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
        ) {
            Text(
                text = stringResource(R.string.confirmation_confirm),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun ConfirmationRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, color = Navy, fontWeight = FontWeight.SemiBold)
    }
}
