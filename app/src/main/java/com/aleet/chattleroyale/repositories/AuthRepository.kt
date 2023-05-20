package com.aleet.chattleroyale.repositories

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import io.grpc.internal.SharedResourceHolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepository: AuthRepositoryInterface {

    private val firebaseAuth = FirebaseAuth.getInstance()
    override fun googleSignIn(credential: AuthCredential): Flow<SharedResourceHolder.Resource<AuthResult>> {
        return flow {
            firebaseAuth.signInWithCredential(credential).await()
        }
    }
}