package com.aleet.chattleroyale.repositories

import com.aleet.chattleroyale.models.User
import com.aleet.chattleroyale.requestModels.LoginRequest
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot

interface DatabaseAuthRepoInterface {
    fun createUser(createUserRequest: LoginRequest): Task<AuthResult>
    fun login(loginRequest: LoginRequest): Task<AuthResult>
    fun getCurrentUser(): FirebaseUser?
    fun sendPasswordResetRequest(email: String): Task<Void>
    fun verifyPasswordResetCode(code: String): Task<String>
    fun createCRUser(userRequest: User)
    fun getCrUser(id: String): Task<DocumentSnapshot>

}