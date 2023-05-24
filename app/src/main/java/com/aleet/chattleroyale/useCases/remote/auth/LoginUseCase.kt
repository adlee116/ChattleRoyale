package com.aleet.chattleroyale.useCases.remote.auth

import com.aleet.chattleroyale.repositories.DatabaseAuthRepoInterface
import com.aleet.chattleroyale.requestModels.LoginRequest
import com.aleet.chattleroyale.utils.BaseUseCase
import com.aleet.chattleroyale.utils.Result
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val databaseAuthRepoInterface: DatabaseAuthRepoInterface
): BaseUseCase<Task<AuthResult>, LoginRequest>() {
    override suspend fun run(params: LoginRequest): Result<Task<AuthResult>, Exception> {
        return try {
            val request = databaseAuthRepoInterface.login(params)
            return Result.Success(request)
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

}

