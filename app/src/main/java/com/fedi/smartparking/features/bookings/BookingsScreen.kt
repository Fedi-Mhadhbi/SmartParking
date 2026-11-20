package com.fedi.smartparking.features.bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fedi.smartparking.R
import com.fedi.smartparking.features.reservation.DemoReservationStore
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue

private data class BookingUiModel(
    val parking: String,
    val address: String,
    val date: String,
    val duration: String,
    val spot: String,
    val total: String,
    val status: BookingStatus,
)

private enum class BookingStatus {
    UPCOMING,
    COMPLETED,
    CANCELED,
}

@Composable
fun BookingsScreen(
    onBookingClick: (String) -> Unit = {},
) {
    val context = LocalContext.current
    val bookings = DemoReservationStore.history.map { reservation ->
        BookingUiModel(
            parking = reservation.parkingName,
            address = "Tunis, Tunisia",
            date = java.text.SimpleDateFormat(
                "dd MMMM yyyy · HH:mm",
                java.util.Locale.getDefault(),
            ).format(java.util.Date(reservation.startAtMillis)),
            duration = context.getString(R.string.reservation_hours, reservation.durationHours),
            spot = reservation.parkingSpot,
            total = context.getString(
                R.string.home_per_hour,
                reservation.hourlyPrice * reservation.durationHours,
            ),
            status = when {
                reservation == DemoReservationStore.current && DemoReservationStore.isCancelled ->
                    BookingStatus.CANCELED
                reservation.startAtMillis <= System.currentTimeMillis() ->
                    BookingStatus.COMPLETED
                else -> BookingStatus.UPCOMING
            },
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 20.dp,
            vertical = 18.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = stringResource(R.string.bookings_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Navy,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.bookings_subtitle),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        item {
            Text(
                text = stringResource(R.string.bookings_upcoming),
                style = MaterialTheme.typography.titleLarge,
                color = Navy,
                fontWeight = FontWeight.Bold,
            )
        }
        items(bookings.filter { it.status == BookingStatus.UPCOMING }) { booking ->
            BookingCard(booking = booking, onClick = { onBookingClick(booking.parking) })
        }
        item {
            Text(
                text = stringResource(R.string.bookings_history),
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.titleLarge,
                color = Navy,
                fontWeight = FontWeight.Bold,
            )
        }
        items(bookings.filter { it.status != BookingStatus.UPCOMING }) { booking ->
            BookingCard(booking = booking, onClick = { onBookingClick(booking.parking) })
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
private fun BookingCard(
    booking: BookingUiModel,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(11.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    color = PrimaryBlue.copy(alpha = 0.12f),
                    shape = CircleShape,
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.padding(10.dp).size(24.dp),
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Text(
                        text = booking.parking,
                        style = MaterialTheme.typography.titleMedium,
                        color = Navy,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = booking.address,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                StatusBadge(booking.status)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                BookingInfo(
                    icon = Icons.Default.CalendarMonth,
                    value = booking.date,
                )
                Text(
                    text = booking.total,
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = booking.duration,
                    modifier = Modifier.padding(start = 5.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = stringResource(R.string.booking_spot_value, booking.spot),
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun BookingInfo(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = value,
            modifier = Modifier.padding(start = 5.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun StatusBadge(status: BookingStatus) {
    val isUpcoming = status == BookingStatus.UPCOMING
    Surface(
        color = when (status) {
            BookingStatus.UPCOMING -> Color(0xFFE6F7F1)
            BookingStatus.CANCELED -> Color(0xFFFFE9E9)
            BookingStatus.COMPLETED -> Color(0xFFF0F2F5)
        },
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = stringResource(
                when (status) {
                    BookingStatus.UPCOMING -> R.string.bookings_upcoming_status
                    BookingStatus.CANCELED -> R.string.bookings_canceled_status
                    BookingStatus.COMPLETED -> R.string.bookings_completed_status
                },
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            color = when (status) {
                BookingStatus.UPCOMING -> Color(0xFF168A68)
                BookingStatus.CANCELED -> Color(0xFFB42318)
                BookingStatus.COMPLETED -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
        )
    }
}
