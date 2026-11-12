package com.fedi.smartparking.features.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.fedi.smartparking.R
import com.fedi.smartparking.ui.components.SmartButton
import com.fedi.smartparking.ui.components.SmartTextField
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue
import com.fedi.smartparking.ui.theme.SmartParkingSpacing

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    onRegister: () -> Unit,
    onForgotPassword: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    AuthFormLayout(
        title = stringResource(R.string.auth_login_title),
        body = stringResource(R.string.auth_login_body),
    ) {
        SmartTextField(
            value = email,
            onValueChange = { email = it },
            label = stringResource(R.string.auth_email),
            leadingIcon = Icons.Default.Email,
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Email,
            ),
        )
        SmartTextField(
            value = password,
            onValueChange = { password = it },
            label = stringResource(R.string.auth_password),
            leadingIcon = Icons.Default.Lock,
            trailingIcon = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
            onTrailingIconClick = { passwordVisible = !passwordVisible },
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
        )
        Text(
            text = stringResource(R.string.auth_forgot_password),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onForgotPassword),
            color = PrimaryBlue,
            style = MaterialTheme.typography.labelLarge,
        )
        SmartButton(
            text = stringResource(R.string.auth_continue),
            onClick = { onLogin(email, password) },
        )
        AuthFooter(
            prompt = stringResource(R.string.auth_no_account),
            action = stringResource(R.string.auth_create_account),
            onClick = onRegister,
        )
    }
}

@Composable
fun AuthFormLayout(
    title: String,
    body: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SmartParkingSpacing.screenHorizontal),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall,
            color = Navy,
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Composable
fun AuthFooter(
    prompt: String,
    action: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(text = prompt, color = Color.Gray)
        Text(
            text = " $action",
            color = PrimaryBlue,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
