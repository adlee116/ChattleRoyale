package com.aleet.chattleroyale.useCases

import com.aleet.chattleroyale.repositories.AuthRepository
import com.aleet.chattleroyale.utils.BaseUseCase
import com.aleet.chattleroyale.utils.Result
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import io.grpc.internal.SharedResourceHolder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
): BaseUseCase<Flow<SharedResourceHolder.Resource<AuthResult>>, AuthCredential>() {
    override suspend fun run(params: AuthCredential): Result<Flow<SharedResourceHolder.Resource<AuthResult>>, Exception> {
        return try {
            Result.Success(authRepository.googleSignIn(params))
        } catch (ex: Exception) {
            Result.Failure(ex)
        }

    }
}