package com.fedi.smartparking.features.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fedi.smartparking.R
import com.fedi.smartparking.ui.components.SmartButton
import com.fedi.smartparking.ui.components.SmartTextField

@Composable
fun ForgotPasswordScreen(
    onSendLink: (String) -> Unit,
    onBackToLogin: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }

    AuthFormLayout(
        title = stringResource(R.string.auth_forgot_title),
        body = stringResource(R.string.auth_forgot_body),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SmartTextField(
                value = email,
                onValueChange = { email = it },
                label = stringResource(R.string.auth_email),
                leadingIcon = Icons.Default.Email,
            )
            SmartButton(
                text = stringResource(R.string.auth_send_link),
                onClick = { onSendLink(email) },
            )
            AuthFooter(
                prompt = "",
                action = stringResource(R.string.auth_back_to_login),
                onClick = onBackToLogin,
            )
        }
    }
}
