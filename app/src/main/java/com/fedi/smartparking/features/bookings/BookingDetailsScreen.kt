package com.fedi.smartparking.features.bookings

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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.fedi.smartparking.features.reservation.DemoReservationStore
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue

@Composable
fun BookingDetailsScreen(
    parkingName: String,
    onBack: () -> Unit,
    onShowQr: () -> Unit,
) {
    val booking = DemoReservationStore.current
    val isCurrentBooking = booking?.parkingName == parkingName
    val canCancel = isCurrentBooking &&
        !DemoReservationStore.isCancelled &&
        booking.startAtMillis - System.currentTimeMillis() >= 60 * 60 * 1000L
    var showCancelDialog by remember { mutableStateOf(false) }

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
                text = stringResource(R.string.booking_details_title),
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
                    imageVector = Icons.Default.QrCode2,
                    contentDescription = null,
                    tint = Color(0xFF168A68),
                )
                Text(
                    text = if (DemoReservationStore.isCancelled && isCurrentBooking) {
                        stringResource(R.string.bookings_canceled_status)
                    } else {
                        stringResource(R.string.bookings_upcoming_status)
                    },
                    color = if (DemoReservationStore.isCancelled && isCurrentBooking) {
                        Color(0xFFB42318)
                    } else {
                        Color(0xFF168A68)
                    },
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.booking_details_reference),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "SP-2026-00124",
                    color = Navy,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Text(
            text = parkingName,
            style = MaterialTheme.typography.titleLarge,
            color = Navy,
            fontWeight = FontWeight.Bold,
        )
        BookingDetailRow(
            icon = Icons.Default.LocationOn,
            label = stringResource(R.string.booking_details_location),
            value = "Avenue Habib Bourguiba, Tunis",
        )
        BookingDetailRow(
            icon = Icons.Default.CalendarMonth,
            label = stringResource(R.string.confirmation_date),
            value = booking?.takeIf { isCurrentBooking }?.let {
                val date = java.text.SimpleDateFormat(
                    "dd MMMM yyyy · HH:mm",
                    java.util.Locale.getDefault(),
                ).format(java.util.Date(it.startAtMillis))
                "$date · ${it.durationHours} hours"
            } ?: "13 November 2026 · 2 hours",
        )
        BookingDetailRow(
            icon = Icons.Default.QrCode2,
            label = stringResource(R.string.confirmation_vehicle),
            value = booking?.takeIf { isCurrentBooking }?.vehicleName
                ?: stringResource(R.string.reservation_no_vehicle),
        )
        BookingDetailRow(
            icon = Icons.Default.QrCode2,
            label = stringResource(R.string.confirmation_spot),
            value = booking?.takeIf { isCurrentBooking }?.parkingSpot
                ?: stringResource(R.string.reservation_no_spot),
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(stringResource(R.string.confirmation_total), color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    booking?.takeIf { isCurrentBooking }?.let {
                        stringResource(
                            R.string.home_per_hour,
                            it.hourlyPrice * it.durationHours,
                        )
                    } ?: "5.000 DT",
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onShowQr,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
        ) {
            Text(
                text = stringResource(R.string.booking_details_show_qr),
                fontWeight = FontWeight.Bold,
            )
        }
        if (isCurrentBooking) {
            Button(
                onClick = { showCancelDialog = true },
                enabled = canCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB42318),
                    disabledContainerColor = Color(0xFFE5B8B5),
                ),
            ) {
                Text(
                    text = when {
                        DemoReservationStore.isCancelled -> stringResource(R.string.booking_cancelled)
                        canCancel -> stringResource(R.string.booking_cancel)
                        else -> stringResource(R.string.booking_cancel_too_late)
                    },
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text(stringResource(R.string.booking_cancel_confirm_title)) },
            text = { Text(stringResource(R.string.booking_cancel_confirm_message)) },
            confirmButton = {
                Button(
                    onClick = {
                        DemoReservationStore.cancelCurrent()
                        showCancelDialog = false
                    },
                ) {
                    Text(stringResource(R.string.booking_cancel_confirm))
                }
            },
            dismissButton = {
                Button(onClick = { showCancelDialog = false }) {
                    Text(stringResource(R.string.reservation_cancel))
                }
            },
        )
    }
}

@Composable
private fun BookingDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = PrimaryBlue)
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, color = Navy)
        }
    }
}
