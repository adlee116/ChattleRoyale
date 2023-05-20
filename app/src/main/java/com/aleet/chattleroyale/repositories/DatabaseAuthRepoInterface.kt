package com.squadsandshots_android.repositories


import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.squadsandshots_android.requestModels.LoginRequest

interface DatabaseAuthRepoInterface {
    fun createUser(createUserRequest: LoginRequest): Task<AuthResult>
    fun login(loginRequest: LoginRequest): Task<AuthResult>
    fun getCurrentUser(): FirebaseUser?
    fun sendPasswordResetRequest(email: String): Task<Void>
    fun verifyPasswordResetCode(code: String): Task<String>
}