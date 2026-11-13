package com.fedi.smartparking.features.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
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

private data class NotificationItem(
    val title: String,
    val message: String,
    val time: String,
    val isUnread: Boolean,
    val completed: Boolean = false,
)

@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    val notifications = listOf(
        NotificationItem(
            title = stringResource(R.string.notification_booking_confirmed_title),
            message = stringResource(R.string.notification_booking_confirmed_message),
            time = stringResource(R.string.notification_today),
            isUnread = true,
            completed = true,
        ),
        NotificationItem(
            title = stringResource(R.string.notification_reminder_title),
            message = stringResource(R.string.notification_reminder_message),
            time = stringResource(R.string.notification_yesterday),
            isUnread = true,
        ),
        NotificationItem(
            title = stringResource(R.string.notification_welcome_title),
            message = stringResource(R.string.notification_welcome_message),
            time = stringResource(R.string.notification_last_week),
            isUnread = false,
        ),
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 20.dp,
            vertical = 14.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.explore_back),
                        tint = Navy,
                    )
                }
                Column(modifier = Modifier.padding(start = 4.dp)) {
                    Text(
                        text = stringResource(R.string.notifications_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Navy,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stringResource(R.string.notifications_subtitle),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        items(notifications) { notification ->
            NotificationCard(notification)
        }
    }
}

@Composable
private fun NotificationCard(notification: NotificationItem) {
    Surface(
        color = if (notification.isUnread) Color.White else Color(0xFFF7F9FC),
        shape = RoundedCornerShape(18.dp),
        shadowElevation = if (notification.isUnread) 2.dp else 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Surface(
                color = if (notification.completed) {
                    Color(0xFFE6F7F1)
                } else {
                    PrimaryBlue.copy(alpha = 0.12f)
                },
                shape = CircleShape,
            ) {
                Icon(
                    imageVector = if (notification.completed) {
                        Icons.Default.CheckCircle
                    } else {
                        Icons.Default.Schedule
                    },
                    contentDescription = null,
                    tint = if (notification.completed) Color(0xFF168A68) else PrimaryBlue,
                    modifier = Modifier.padding(10.dp).size(22.dp),
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text(
                    text = notification.title,
                    color = Navy,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = notification.message,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = notification.time,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            if (notification.isUnread) {
                Surface(
                    modifier = Modifier.size(8.dp),
                    color = PrimaryBlue,
                    shape = CircleShape,
                ) {}
            }
        }
    }
}
