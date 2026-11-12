package com.fedi.smartparking.features.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.fedi.smartparking.R
import com.fedi.smartparking.ui.components.SmartButton
import com.fedi.smartparking.ui.components.SmartTextField

@Composable
fun RegisterScreen(
    onRegister: (String, String, String) -> Unit,
    onLogin: () -> Unit,
) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var acceptedTerms by rememberSaveable { mutableStateOf(false) }

    AuthFormLayout(
        title = stringResource(R.string.auth_register_title),
        body = stringResource(R.string.auth_register_body),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SmartTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = stringResource(R.string.auth_full_name),
                leadingIcon = Icons.Default.Person,
            )
            SmartTextField(
                value = email,
                onValueChange = { email = it },
                label = stringResource(R.string.auth_email),
                leadingIcon = Icons.Default.Email,
            )
            SmartTextField(
                value = phone,
                onValueChange = { phone = it },
                label = stringResource(R.string.auth_phone),
                leadingIcon = Icons.Default.Phone,
            )
            SmartTextField(
                value = password,
                onValueChange = { password = it },
                label = stringResource(R.string.auth_password),
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation(),
            )
            SmartTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = stringResource(R.string.auth_confirm_password),
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation(),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Checkbox(
                    checked = acceptedTerms,
                    onCheckedChange = { acceptedTerms = it },
                )
                Text(
                    text = stringResource(R.string.auth_accept_terms),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            SmartButton(
                text = stringResource(R.string.auth_create_account),
                onClick = { onRegister(fullName, email, password) },
                enabled = acceptedTerms,
            )
            AuthFooter(
                prompt = stringResource(R.string.auth_have_account),
                action = stringResource(R.string.auth_sign_in),
                onClick = onLogin,
            )
        }
    }
}
