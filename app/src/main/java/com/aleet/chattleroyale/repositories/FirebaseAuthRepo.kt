package com.aleet.chattleroyale.repositories

import android.util.Log
import com.aleet.chattleroyale.models.User
import com.aleet.chattleroyale.requestModels.LoginRequest
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseAuthRepo @Inject constructor(): DatabaseAuthRepoInterface {

    private val TAG = "firestore call"

    private val firestore = Firebase.firestore
    private val firebaseAuth = Firebase.auth

    override fun createUser(createUserRequest: LoginRequest): Task<AuthResult> {
        return firebaseAuth.createUserWithEmailAndPassword(createUserRequest.username, createUserRequest.password)
    }

    override fun login(loginRequest: LoginRequest): Task<AuthResult> {
        return firebaseAuth.signInWithEmailAndPassword(loginRequest.username, loginRequest.password)
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun sendPasswordResetRequest(email: String): Task<Void> {
        return firebaseAuth.sendPasswordResetEmail(email)
    }

    override fun  verifyPasswordResetCode(code: String): Task<String> {
        return firebaseAuth.verifyPasswordResetCode(code)
    }

    override fun createCRUser(userRequest: User) {
        val firestoreUser = firestore.collection("users").document(userRequest.uid)
        val userData = hashMapOf(
            "username" to userRequest.userName,
            "email" to userRequest.email,
            "profile_picture" to userRequest.profilePicture,
            // Initialize other fields to their default values
            "games_won" to 0,
            "games_played" to 0,
            "last_online" to FieldValue.serverTimestamp(), // Initialize to current time
            "friends" to hashMapOf<String, Boolean>()
        )
        firestoreUser.set(userData).addOnSuccessListener {
            Log.d(TAG, "User successfully created!")
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error creating user", e)
        }
    }


}