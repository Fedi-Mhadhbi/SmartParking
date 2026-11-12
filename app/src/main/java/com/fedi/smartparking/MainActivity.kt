package com.fedi.smartparking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fedi.smartparking.ui.theme.AppTheme
import com.fedi.smartparking.ui.theme.LightBorder
import com.fedi.smartparking.ui.theme.SmartParkingSpacing
import com.fedi.smartparking.ui.theme.SmartParkingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartParkingTheme {
                ThemePreviewScreen()
            }
        }
    }
}

@Composable
private fun ThemePreviewScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .padding(SmartParkingSpacing.screenHorizontal),
            verticalArrangement = Arrangement.spacedBy(SmartParkingSpacing.sectionGap),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(SmartParkingSpacing.compact)) {
                Text(
                    text = stringResource(R.string.theme_preview_title),
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = stringResource(R.string.theme_preview_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.primary_action))
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LightBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = stringResource(R.string.status_colors),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        StatusBadge(stringResource(R.string.available), AppTheme.colors.available)
                        StatusBadge(stringResource(R.string.reserved), AppTheme.colors.reserved)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        StatusBadge(stringResource(R.string.occupied), AppTheme.colors.occupied)
                        StatusBadge(stringResource(R.string.error), AppTheme.colors.error)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(label: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.14f),
        contentColor = color,
        shape = RoundedCornerShape(50),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
