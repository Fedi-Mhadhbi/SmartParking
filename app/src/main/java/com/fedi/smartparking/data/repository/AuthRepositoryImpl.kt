package com.fedi.smartparking.data.repository

import com.fedi.smartparking.data.remote.auth.FirebaseAuthDataSource

class AuthRepositoryImpl(
    private val dataSource: FirebaseAuthDataSource = FirebaseAuthDataSource(),
) {
    fun login(email: String, password: String, onComplete: (Result<Unit>) -> Unit) =
        dataSource.login(email, password, onComplete)

    fun register(
        fullName: String,
        email: String,
        password: String,
        onComplete: (Result<Unit>) -> Unit,
    ) = dataSource.register(fullName, email, password, onComplete)

    fun sendPasswordReset(email: String, onComplete: (Result<Unit>) -> Unit) =
        dataSource.sendPasswordReset(email, onComplete)
}
