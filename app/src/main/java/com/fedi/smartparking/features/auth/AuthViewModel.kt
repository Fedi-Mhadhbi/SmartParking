package com.fedi.smartparking.features.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) return
        _uiState.value = AuthUiState(
            isAuthenticated = true,
            message = "demo-login-success",
        )
    }

    fun register(fullName: String, email: String, password: String) {
        if (fullName.isBlank() || email.isBlank() || password.isBlank()) return
        _uiState.value = AuthUiState(
            isAuthenticated = true,
            message = "demo-login-success",
        )
    }

    fun sendPasswordReset(email: String) {
        if (email.isBlank()) return
        _uiState.value = AuthUiState(message = "demo-reset-sent")
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}
