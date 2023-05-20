package com.squadsandshots_android.useCases

import com.google.android.gms.tasks.Task
import com.squadsandshots_android.core.utils.BaseUseCase
import com.squadsandshots_android.core.utils.Result
import com.squadsandshots_android.repositories.DatabaseAuthRepoInterface
import javax.inject.Inject

class ConfirmPasswordResetUseCase @Inject constructor(val databaseAuthRepoInterface: DatabaseAuthRepoInterface) :
    BaseUseCase<Task<String>, String>() {
    override suspend fun run(params: String): Result<Task<String>, Exception> {
        return try {
            Result.Success(databaseAuthRepoInterface.verifyPasswordResetCode(params))
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}