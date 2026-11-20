package com.fedi.smartparking.features.auth

import androidx.lifecycle.ViewModel
import com.fedi.smartparking.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepositoryImpl()
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) return
        _uiState.value = _uiState.value.copy(isLoading = true, message = null)
        authRepository.login(email, password) { result ->
            _uiState.value = AuthUiState(
                isLoading = false,
                isAuthenticated = result.isSuccess,
                message = result.exceptionOrNull()?.message,
            )
            if (result.isSuccess) onSuccess()
        }
    }

    fun register(fullName: String, email: String, password: String, onSuccess: () -> Unit) {
        if (fullName.isBlank() || email.isBlank() || password.isBlank()) return
        _uiState.value = _uiState.value.copy(isLoading = true, message = null)
        authRepository.register(fullName, email, password) { result ->
            _uiState.value = AuthUiState(
                isLoading = false,
                isAuthenticated = result.isSuccess,
                message = result.exceptionOrNull()?.message,
            )
            if (result.isSuccess) onSuccess()
        }
    }

    fun sendPasswordReset(email: String, onComplete: () -> Unit = {}) {
        if (email.isBlank()) return
        authRepository.sendPasswordReset(email) { result ->
            _uiState.value = AuthUiState(
                message = result.exceptionOrNull()?.message ?: "reset-sent",
            )
            if (result.isSuccess) onComplete()
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}
