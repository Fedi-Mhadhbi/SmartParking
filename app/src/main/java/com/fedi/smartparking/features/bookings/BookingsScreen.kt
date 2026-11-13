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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fedi.smartparking.R
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue

private data class BookingUiModel(
    val parking: String,
    val address: String,
    val date: String,
    val duration: String,
    val total: String,
    val status: BookingStatus,
)

private enum class BookingStatus {
    UPCOMING,
    COMPLETED,
}

@Composable
fun BookingsScreen(
    onBookingClick: (String) -> Unit = {},
) {
    val bookings = listOf(
        BookingUiModel(
            parking = "Parking Avenue",
            address = "Avenue Habib Bourguiba",
            date = "13 November 2026",
            duration = "2 hours",
            total = "5.000 DT",
            status = BookingStatus.UPCOMING,
        ),
        BookingUiModel(
            parking = "Parking Lafayette",
            address = "Rue de Palestine",
            date = "08 November 2026",
            duration = "1 hour",
            total = "2.000 DT",
            status = BookingStatus.COMPLETED,
        ),
    )

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
        items(bookings.filter { it.status == BookingStatus.COMPLETED }) { booking ->
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
        color = if (isUpcoming) Color(0xFFE6F7F1) else Color(0xFFF0F2F5),
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = stringResource(
                if (isUpcoming) R.string.bookings_upcoming_status else R.string.bookings_completed_status,
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            color = if (isUpcoming) Color(0xFF168A68) else MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
        )
    }
}
