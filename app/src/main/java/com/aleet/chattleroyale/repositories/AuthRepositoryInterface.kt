package com.aleet.chattleroyale.repositories

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import io.grpc.internal.SharedResourceHolder
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun googleSignIn(credential: AuthCredential): Flow<SharedResourceHolder.Resource<AuthResult>>
}