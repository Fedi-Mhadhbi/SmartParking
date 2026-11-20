package com.fedi.smartparking.features.payment

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.fedi.smartparking.features.reservation.DemoParkingSpotStore
import com.fedi.smartparking.features.home.ParkingAvailabilityStore
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue
import android.widget.Toast

private enum class PaymentMethod {
    CARD,
    ORANGE,
    OOREDOO,
    TELECOM,
}

@Composable
fun PaymentScreen(
    parkingName: String,
    onBack: () -> Unit,
    onPaymentComplete: () -> Unit,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var method by remember { mutableStateOf(PaymentMethod.CARD) }
    var cardNumber by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val amount = DemoReservationStore.current?.let {
        it.hourlyPrice * it.durationHours
    } ?: 0.0
    val cardValid = cardNumber.filter(Char::isDigit).length >= 12 &&
        expiry.isNotBlank() &&
        cvv.filter(Char::isDigit).length >= 3
    val phoneValid = phoneNumber.filter(Char::isDigit).length == 8

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
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
                text = stringResource(R.string.payment_title),
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
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(stringResource(R.string.payment_total), color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = stringResource(R.string.home_per_hour, amount),
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Text(
            text = stringResource(R.string.payment_method),
            style = MaterialTheme.typography.titleMedium,
            color = Navy,
            fontWeight = FontWeight.Bold,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PaymentChip(stringResource(R.string.payment_card), method == PaymentMethod.CARD) {
                method = PaymentMethod.CARD
            }
            PaymentChip(stringResource(R.string.payment_mobile), method != PaymentMethod.CARD) {
                method = PaymentMethod.ORANGE
            }
        }
        if (method == PaymentMethod.CARD) {
            OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.payment_card_number)) },
                singleLine = true,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = expiry,
                    onValueChange = { expiry = it },
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(R.string.payment_expiry)) },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it },
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(R.string.payment_cvv)) },
                    singleLine = true,
                )
            }
        } else {
            Text(
                text = stringResource(R.string.payment_mobile_hint),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.payment_phone_number)) },
                placeholder = { Text(stringResource(R.string.payment_phone_hint)) },
                singleLine = true,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PaymentChip(stringResource(R.string.payment_orange), method == PaymentMethod.ORANGE) {
                    method = PaymentMethod.ORANGE
                }
                PaymentChip(stringResource(R.string.payment_ooredoo), method == PaymentMethod.OOREDOO) {
                    method = PaymentMethod.OOREDOO
                }
                PaymentChip(stringResource(R.string.payment_telecom), method == PaymentMethod.TELECOM) {
                    method = PaymentMethod.TELECOM
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                val selection = DemoReservationStore.current
                val spotReserved = selection?.let {
                    DemoParkingSpotStore.reserve(parkingName, it.parkingSpot)
                } == true
                if (spotReserved && ParkingAvailabilityStore.reserve(parkingName, 12)) {
                    onPaymentComplete()
                } else {
                    Toast.makeText(
                        context,
                        R.string.reservation_spot_taken,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            },
            enabled = if (method == PaymentMethod.CARD) cardValid else phoneValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
        ) {
            Text(stringResource(R.string.payment_pay), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PaymentChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
            selectedContainerColor = PrimaryBlue,
            selectedLabelColor = Color.White,
        ),
    )
}
