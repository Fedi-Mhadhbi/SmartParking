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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateMapOf
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
import com.fedi.smartparking.features.vehicles.DemoVehicle
import com.fedi.smartparking.features.vehicles.DemoVehicleStore
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar
import com.google.firebase.auth.FirebaseAuth

data class ReservationSelection(
    val parkingName: String,
    val startAtMillis: Long,
    val durationHours: Int,
    val hourlyPrice: Double,
    val vehicleName: String,
    val parkingSpot: String,
)

data class ParkingSpot(
    val name: String,
    val isAvailable: Boolean,
)

object DemoParkingSpotStore {
    private val unavailableByParking = mutableStateMapOf<String, Set<String>>()

    fun spots(parkingName: String): List<ParkingSpot> {
        val unavailable = unavailableByParking.getOrPut(parkingName) {
            setOf("A3", "A7", "B2", "B8", "C5")
        }
        return listOf("A", "B", "C").flatMap { section ->
            (1..10).map { number ->
                val name = "$section$number"
                ParkingSpot(name, name !in unavailable)
            }
        }
    }

    fun reserve(parkingName: String, spotName: String): Boolean {
        val unavailable = unavailableByParking.getOrPut(parkingName) {
            setOf("A3", "A7", "B2", "B8", "C5")
        }
        if (spotName in unavailable) return false
        unavailableByParking[parkingName] = unavailable + spotName
        return true
    }
}

object DemoReservationStore {
    private val histories = mutableStateMapOf<String, List<ReservationSelection>>()
    private val currentReservations = mutableStateMapOf<String, ReservationSelection>()
    private val cancelledReservations = mutableStateMapOf<String, Boolean>()

    private val userKey: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"

    val current: ReservationSelection?
        get() = currentReservations[userKey]

    val isCancelled: Boolean
        get() = cancelledReservations[userKey] == true

    val history: List<ReservationSelection>
        get() = histories[userKey].orEmpty()

    fun add(selection: ReservationSelection) {
        currentReservations[userKey] = selection
        cancelledReservations[userKey] = false
        histories[userKey] = history + selection
    }

    fun cancelCurrent() {
        cancelledReservations[userKey] = true
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ReservationScreen(
    parkingName: String,
    onBack: () -> Unit,
    onConfirm: (
        startAtMillis: Long,
        durationHours: Int,
        hourlyPrice: Double,
        vehicleName: String,
        parkingSpot: String,
    ) -> Unit,
) {
    var duration by remember { mutableIntStateOf(1) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var selectedHour by remember { mutableStateOf<Int?>(null) }
    var showTimeMenu by remember { mutableStateOf(false) }
    var selectedVehicle by remember { mutableStateOf<DemoVehicle?>(null) }
    var showVehicleMenu by remember { mutableStateOf(false) }
    var selectedSpot by remember { mutableStateOf<String?>(null) }
    val vehicles = DemoVehicleStore.vehicles
    val spots = DemoParkingSpotStore.spots(parkingName)
    val hourlyPrice = when (parkingName) {
        "Parking Lafayette" -> 2.0
        "Parking Municipal" -> 1.8
        else -> 2.5
    }
    val selectedDate = selectedDateMillis?.let { millis ->
        SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(millis))
    } ?: stringResource(R.string.reservation_select_date)
    val selectedTime = selectedHour?.let { String.format(Locale.getDefault(), "%02d:00", it) }
        ?: stringResource(R.string.reservation_select_time)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
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

        Text(
            text = stringResource(R.string.reservation_vehicle),
            style = MaterialTheme.typography.titleMedium,
            color = Navy,
            fontWeight = FontWeight.Bold,
        )
        Box {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shape = RoundedCornerShape(16.dp),
                onClick = { showVehicleMenu = true },
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(24.dp),
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                    ) {
                        Text(
                            text = selectedVehicle?.name
                                ?: stringResource(R.string.reservation_no_vehicle),
                            color = Navy,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        selectedVehicle?.let {
                            Text(
                                text = when (it.type) {
                                    com.fedi.smartparking.features.vehicles.DemoVehicleType.CAR ->
                                        stringResource(R.string.vehicle_car)
                                    com.fedi.smartparking.features.vehicles.DemoVehicleType.MOTORCYCLE ->
                                        stringResource(R.string.vehicle_motorcycle)
                                    com.fedi.smartparking.features.vehicles.DemoVehicleType.VAN ->
                                        stringResource(R.string.vehicle_van)
                                },
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Navy,
                    )
                }
            }
            DropdownMenu(
                expanded = showVehicleMenu,
                onDismissRequest = { showVehicleMenu = false },
            ) {
                vehicles.forEach { vehicle ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                "${vehicle.name} · ${
                                    when (vehicle.type) {
                                        com.fedi.smartparking.features.vehicles.DemoVehicleType.CAR ->
                                            stringResource(R.string.vehicle_car)
                                        com.fedi.smartparking.features.vehicles.DemoVehicleType.MOTORCYCLE ->
                                            stringResource(R.string.vehicle_motorcycle)
                                        com.fedi.smartparking.features.vehicles.DemoVehicleType.VAN ->
                                            stringResource(R.string.vehicle_van)
                                    }
                                }",
                            )
                        },
                        onClick = {
                            selectedVehicle = vehicle
                            showVehicleMenu = false
                        },
                    )
                }
            }
        }

        ReservationField(
            icon = Icons.Default.CalendarMonth,
            label = stringResource(R.string.reservation_date),
            value = selectedDate,
            onClick = { showDatePicker = true },
        )
        Box {
            ReservationField(
                icon = Icons.Default.Schedule,
                label = stringResource(R.string.reservation_time),
                value = selectedTime,
                onClick = { showTimeMenu = true },
            )
            DropdownMenu(
                expanded = showTimeMenu,
                onDismissRequest = { showTimeMenu = false },
            ) {
                (8..22).forEach { hour ->
                    DropdownMenuItem(
                        text = {
                            Text(String.format(Locale.getDefault(), "%02d:00", hour))
                        },
                        onClick = {
                            selectedHour = hour
                            showTimeMenu = false
                        },
                    )
                }
            }
        }
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

        Text(
            text = stringResource(R.string.reservation_choose_spot),
            style = MaterialTheme.typography.titleMedium,
            color = Navy,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(R.string.reservation_choose_spot_hint),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SpotLegend()
        SpotGrid(
            spots = spots,
            selectedSpot = selectedSpot,
            onSpotSelected = { selectedSpot = it },
        )
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
            onClick = {
                val dateMillis = selectedDateMillis ?: return@Button
                val hour = selectedHour ?: return@Button
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = dateMillis
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val vehicle = selectedVehicle ?: return@Button
                val spot = selectedSpot ?: return@Button
                onConfirm(calendar.timeInMillis, duration, hourlyPrice, vehicle.name, spot)
            },
            enabled = selectedDateMillis != null &&
                selectedHour != null &&
                selectedVehicle != null &&
                selectedSpot != null,
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
private fun SpotLegend() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        LegendItem(
            color = Color(0xFFE9F8F1),
            label = stringResource(R.string.reservation_spot_available),
        )
        LegendItem(
            color = Color(0xFFFFE9E9),
            label = stringResource(R.string.reservation_spot_unavailable),
        )
        LegendItem(
            color = PrimaryBlue,
            label = stringResource(R.string.reservation_spot_selected),
        )
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(14.dp),
            color = color,
            shape = RoundedCornerShape(4.dp),
        ) {}
        Text(
            text = label,
            modifier = Modifier.padding(start = 5.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SpotGrid(
    spots: List<ParkingSpot>,
    selectedSpot: String?,
    onSpotSelected: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        spots.chunked(5).forEach { rowSpots ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                rowSpots.forEach { spot ->
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .size(52.dp),
                        onClick = {
                            if (spot.isAvailable) onSpotSelected(spot.name)
                        },
                        color = when {
                            selectedSpot == spot.name -> PrimaryBlue
                            spot.isAvailable -> Color(0xFFE9F8F1)
                            else -> Color(0xFFFFE9E9)
                        },
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = spot.name,
                                color = when {
                                    selectedSpot == spot.name || !spot.isAvailable -> Color.White
                                    else -> Color(0xFF126B52)
                                },
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
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
