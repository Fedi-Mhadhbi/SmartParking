package com.fedi.smartparking.data.remote.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class FirebaseAuthDataSource(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
) {
    fun login(
        email: String,
        password: String,
        onComplete: (Result<Unit>) -> Unit,
    ) {
        firebaseAuth.signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                onComplete(
                    if (task.isSuccessful) {
                        Result.success(Unit)
                    } else {
                        Result.failure(task.exception ?: IllegalStateException("Authentication failed"))
                    },
                )
            }
    }

    fun register(
        fullName: String,
        email: String,
        password: String,
        onComplete: (Result<Unit>) -> Unit,
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    onComplete(
                        Result.failure(task.exception ?: IllegalStateException("Registration failed")),
                    )
                    return@addOnCompleteListener
                }
                val profile = UserProfileChangeRequest.Builder()
                    .setDisplayName(fullName.trim())
                    .build()
                firebaseAuth.currentUser?.updateProfile(profile)
                    ?.addOnCompleteListener { profileTask ->
                        onComplete(
                            if (profileTask.isSuccessful) {
                                Result.success(Unit)
                            } else {
                                Result.failure(
                                    profileTask.exception
                                        ?: IllegalStateException("Profile update failed"),
                                )
                            },
                        )
                    }
                    ?: onComplete(Result.success(Unit))
            }
    }

    fun sendPasswordReset(
        email: String,
        onComplete: (Result<Unit>) -> Unit,
    ) {
        firebaseAuth.sendPasswordResetEmail(email.trim())
            .addOnCompleteListener { task ->
                onComplete(
                    if (task.isSuccessful) {
                        Result.success(Unit)
                    } else {
                        Result.failure(task.exception ?: IllegalStateException("Password reset failed"))
                    },
                )
            }
    }
}
