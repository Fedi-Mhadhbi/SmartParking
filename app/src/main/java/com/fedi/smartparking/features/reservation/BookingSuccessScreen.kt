package com.fedi.smartparking.features.reservation

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fedi.smartparking.R
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import android.graphics.Bitmap
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BookingSuccessScreen(
    parkingName: String,
    onDone: () -> Unit,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val receipt = remember(parkingName, DemoReservationStore.current) {
        createReceipt(parkingName)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF168A68),
            modifier = Modifier.size(72.dp),
        )
        Text(
            text = stringResource(R.string.success_title),
            style = MaterialTheme.typography.headlineSmall,
            color = Navy,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp),
        )
        Text(
            text = stringResource(R.string.success_subtitle),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp),
        )
        Text(
            text = parkingName,
            color = Navy,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 22.dp),
        )
        DemoReservationStore.current?.let { selection ->
            Text(
                text = stringResource(
                    R.string.success_vehicle_spot,
                    selection.vehicleName,
                    selection.parkingSpot,
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
        QrPreview(
            payload = DemoReservationStore.current?.let {
                "SP-2026-00124|$parkingName|${it.parkingSpot}"
            } ?: "SP-2026-00124",
            modifier = Modifier.padding(top = 20.dp),
        )
        Text(
            text = stringResource(R.string.success_reference),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 14.dp),
        )
        Text(
            text = "SP-2026-00124",
            color = Navy,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            OutlinedButton(
                onClick = {
                    context.startActivity(
                        Intent.createChooser(
                            Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, receipt)
                            },
                            context.getString(R.string.receipt_share),
                        ),
                    )
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Text(
                    text = stringResource(R.string.receipt_share),
                    modifier = Modifier.padding(start = 6.dp),
                )
            }
            OutlinedButton(
                onClick = {
                    saveReceipt(context, receipt)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
            ) {
                Icon(Icons.Default.Download, contentDescription = null)
                Text(
                    text = stringResource(R.string.receipt_download),
                    modifier = Modifier.padding(start = 6.dp),
                )
            }
        }
        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = stringResource(R.string.success_done),
                fontWeight = FontWeight.Bold,
            )
        }
        }
    }

private fun createReceipt(parkingName: String): String {
    val selection = DemoReservationStore.current
    val date = selection?.startAtMillis?.let {
        SimpleDateFormat("dd MMMM yyyy · HH:mm", Locale.getDefault()).format(Date(it))
    } ?: "N/A"
    val duration = selection?.durationHours ?: 0
    val total = selection?.let { it.hourlyPrice * it.durationHours } ?: 0.0
    return """
        EasyPar - Reservation receipt
        Reference: SP-2026-00124
        Parking: $parkingName
        Date and time: $date
        Duration: $duration hour(s)
        Vehicle: ${selection?.vehicleName ?: "N/A"}
        Parking place: ${selection?.parkingSpot ?: "N/A"}
        Total: ${"%.3f".format(Locale.US, total)} DT
    """.trimIndent()
}

private fun saveReceipt(context: Context, content: String) {
    val fileName = "easypar-receipt-SP-2026-00124.txt"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val values = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        put(MediaStore.Downloads.MIME_TYPE, "text/plain")
        put(MediaStore.Downloads.IS_PENDING, 1)
        }
        val uri = context.contentResolver.insert(
        MediaStore.Downloads.EXTERNAL_CONTENT_URI,
        values,
        )
        if (uri == null) {
        Toast.makeText(context, R.string.receipt_download_failed, Toast.LENGTH_SHORT).show()
        return
        }
        context.contentResolver.openOutputStream(uri)?.use { output ->
        output.write(content.toByteArray())
        }
        values.clear()
        values.put(MediaStore.Downloads.IS_PENDING, 0)
        context.contentResolver.update(uri, values, null, null)
    } else {
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = java.io.File(directory, fileName)
        file.writeText(content)
    }
    Toast.makeText(context, R.string.receipt_downloaded, Toast.LENGTH_SHORT).show()
}

@Composable
private fun QrPreview(payload: String, modifier: Modifier = Modifier) {
    val qrBitmap = remember {
        QRCodeWriter()
            .encode(payload, BarcodeFormat.QR_CODE, 420, 420)
            .toBitmap()
    }
    Image(
        bitmap = qrBitmap.asImageBitmap(),
        contentDescription = stringResource(R.string.success_qr_description),
        modifier = modifier
            .size(190.dp)
            .background(Color.White, RoundedCornerShape(14.dp))
            .padding(14.dp),
    )
}

private fun com.google.zxing.common.BitMatrix.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    for (x in 0 until width) {
        for (y in 0 until height) {
            bitmap.setPixel(
                x,
                y,
                if (get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE,
            )
        }
    }
    return bitmap
}
