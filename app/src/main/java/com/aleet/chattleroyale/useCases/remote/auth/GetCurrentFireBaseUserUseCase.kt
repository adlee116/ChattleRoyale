package com.aleet.chattleroyale.useCases.remote.auth

import com.aleet.chattleroyale.repositories.DatabaseAuthRepoInterface
import com.aleet.chattleroyale.utils.EmptyParamsUseCase
import com.aleet.chattleroyale.utils.Result
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class GetCurrentFireBaseUserUseCase @Inject constructor(
    private val databaseAuthRepoInterface: DatabaseAuthRepoInterface
): EmptyParamsUseCase<Result<FirebaseUser?, Exception>>() {

    override suspend fun run(): Result<FirebaseUser?, Exception> {
        return try {
            Result.Success(databaseAuthRepoInterface.getCurrentUser())
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}