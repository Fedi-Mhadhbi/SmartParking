package com.fedi.smartparking.features.reservation

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

@Composable
fun BookingSuccessScreen(
    parkingName: String,
    onDone: () -> Unit,
) {
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
        QrPreview(modifier = Modifier.padding(top = 20.dp))
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

@Composable
private fun QrPreview(modifier: Modifier = Modifier) {
    val qrBitmap = remember {
        QRCodeWriter()
            .encode("SP-2026-00124", BarcodeFormat.QR_CODE, 420, 420)
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
