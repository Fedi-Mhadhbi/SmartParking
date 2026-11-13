package com.fedi.smartparking.features.reservation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fedi.smartparking.R
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ReservationScreen(
    parkingName: String,
    onBack: () -> Unit,
    onConfirm: () -> Unit,
) {
    var duration by remember { mutableIntStateOf(1) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    val hourlyPrice = when (parkingName) {
        "Parking Lafayette" -> 2.0
        "Parking Municipal" -> 1.8
        else -> 2.5
    }
    val selectedDate = selectedDateMillis?.let { millis ->
        SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(millis))
    } ?: stringResource(R.string.reservation_select_date)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
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
                text = stringResource(R.string.reservation_title),
                style = MaterialTheme.typography.headlineSmall,
                color = Navy,
                fontWeight = FontWeight.Bold,
            )
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shape = RoundedCornerShape(18.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text(
                    text = parkingName,
                    style = MaterialTheme.typography.titleLarge,
                    color = Navy,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.reservation_location),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        ReservationField(
            icon = Icons.Default.CalendarMonth,
            label = stringResource(R.string.reservation_date),
            value = selectedDate,
            onClick = { showDatePicker = true },
        )
        ReservationField(
            icon = Icons.Default.DirectionsCar,
            label = stringResource(R.string.reservation_vehicle),
            value = stringResource(R.string.reservation_default_vehicle),
            onClick = {},
        )

        Text(
            text = stringResource(R.string.reservation_duration),
            style = MaterialTheme.typography.titleMedium,
            color = Navy,
            fontWeight = FontWeight.Bold,
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(
                    onClick = { if (duration > 1) duration-- },
                    modifier = Modifier.background(Color(0xFFEAF2FB), RoundedCornerShape(12.dp)),
                ) {
                    Icon(Icons.Default.Remove, contentDescription = null, tint = PrimaryBlue)
                }
                Text(
                    text = stringResource(R.string.reservation_hours, duration),
                    style = MaterialTheme.typography.titleLarge,
                    color = Navy,
                    fontWeight = FontWeight.Bold,
                )
                IconButton(
                    onClick = { if (duration < 12) duration++ },
                    modifier = Modifier.background(PrimaryBlue, RoundedCornerShape(12.dp)),
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shape = RoundedCornerShape(18.dp),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.reservation_total),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = stringResource(R.string.home_per_hour, hourlyPrice * duration),
                    color = Navy,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Button(
            onClick = onConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
        ) {
            Text(
                text = stringResource(R.string.reservation_confirm),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis,
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDateMillis = datePickerState.selectedDateMillis
                        showDatePicker = false
                    },
                ) {
                    Text(stringResource(R.string.reservation_select))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.reservation_cancel))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun ReservationField(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(value, style = MaterialTheme.typography.titleMedium, color = Navy)
            }
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Navy)
        }
    }
}
