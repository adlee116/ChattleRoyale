package com.aleet.chattleroyale.useCases.remoteAuth

import com.aleet.chattleroyale.repositories.DatabaseAuthRepoInterface
import com.aleet.chattleroyale.requestModels.LoginRequest
import com.aleet.chattleroyale.utils.BaseUseCase
import com.aleet.chattleroyale.utils.Result
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class CreateFireBaseUserUseCase @Inject constructor(
    private val databaseAuthRepoInterface: DatabaseAuthRepoInterface
) : BaseUseCase<Task<AuthResult>, LoginRequest>() {
    override suspend fun run(params: LoginRequest): Result<Task<AuthResult>, Exception> {
        return try {
            Result.Success(databaseAuthRepoInterface.createUser(params))
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}
