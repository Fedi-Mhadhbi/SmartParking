package com.fedi.smartparking.features.auth

data class AuthUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val isAuthenticated: Boolean = false,
)
