package com.fedi.smartparking.features.vehicles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue
import com.google.firebase.auth.FirebaseAuth

enum class DemoVehicleType {
    CAR,
    MOTORCYCLE,
    VAN,
}

data class DemoVehicle(
    val name: String,
    val type: DemoVehicleType,
)

object DemoVehicleStore {
    private val vehiclesByUser = mutableStateMapOf<String, List<DemoVehicle>>()

    private val userKey: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"

    val vehicles: List<DemoVehicle>
        get() = vehiclesByUser.getOrPut(userKey) {
            listOf(DemoVehicle("My car", DemoVehicleType.CAR))
        }

    fun add(name: String, type: DemoVehicleType) {
        val cleanName = name.trim()
        if (cleanName.isEmpty()) return
        vehiclesByUser[userKey] = vehicles + DemoVehicle(cleanName, type)
    }

    fun remove(vehicle: DemoVehicle) {
        vehiclesByUser[userKey] = vehicles.filterNot { it == vehicle }
    }

}

@Composable
fun MyVehiclesScreen(onBack: () -> Unit) {
    var showAddDialog by remember { mutableStateOf(false) }
    val vehicles = DemoVehicleStore.vehicles

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
                text = stringResource(R.string.vehicles_title),
                style = MaterialTheme.typography.headlineSmall,
                color = Navy,
                fontWeight = FontWeight.Bold,
            )
        }
        Text(
            text = stringResource(R.string.vehicles_subtitle),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(vehicles) { vehicle ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shape = RoundedCornerShape(18.dp),
                    shadowElevation = 2.dp,
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(30.dp),
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 14.dp),
                        ) {
                            Text(
                                text = vehicle.name,
                                color = Navy,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = stringResource(
                                    R.string.vehicle_type_value,
                                    vehicleTypeLabel(vehicle.type),
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        IconButton(onClick = { DemoVehicleStore.remove(vehicle) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.vehicle_delete),
                                tint = Color(0xFFB42318),
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Text(
                text = stringResource(R.string.vehicle_add),
                modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.Bold,
            )
        }
    }

    if (showAddDialog) {
        AddVehicleDialog(onDismiss = { showAddDialog = false })
    }
}

@Composable
private fun AddVehicleDialog(onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(DemoVehicleType.CAR) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.vehicle_add_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.vehicle_name)) },
                    singleLine = true,
                )
                Text(
                    text = stringResource(R.string.vehicle_type),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    VehicleTypeChip(
                        text = stringResource(R.string.vehicle_car),
                        selected = type == DemoVehicleType.CAR,
                        onClick = { type = DemoVehicleType.CAR },
                    )
                    VehicleTypeChip(
                        text = stringResource(R.string.vehicle_motorcycle),
                        selected = type == DemoVehicleType.MOTORCYCLE,
                        onClick = { type = DemoVehicleType.MOTORCYCLE },
                    )
                    VehicleTypeChip(
                        text = stringResource(R.string.vehicle_van),
                        selected = type == DemoVehicleType.VAN,
                        onClick = { type = DemoVehicleType.VAN },
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    DemoVehicleStore.add(name, type)
                    onDismiss()
                },
                enabled = name.isNotBlank(),
            ) {
                Text(stringResource(R.string.vehicle_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.reservation_cancel))
            }
        },
    )
}

@Composable
private fun vehicleTypeLabel(type: DemoVehicleType): String = when (type) {
    DemoVehicleType.CAR -> stringResource(R.string.vehicle_car)
    DemoVehicleType.MOTORCYCLE -> stringResource(R.string.vehicle_motorcycle)
    DemoVehicleType.VAN -> stringResource(R.string.vehicle_van)
}

@Composable
private fun VehicleTypeChip(text: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) },
        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
            selectedContainerColor = PrimaryBlue,
            selectedLabelColor = Color.White,
        ),
    )
}
