package com.squadsandshots_android.useCases

import com.google.android.gms.tasks.Task
import com.squadsandshots_android.core.utils.BaseUseCase
import com.squadsandshots_android.core.utils.Result
import com.squadsandshots_android.repositories.DatabaseAuthRepoInterface
import javax.inject.Inject

class RequestResetPasswordUseCase @Inject constructor(val databaseAuthRepoInterface: DatabaseAuthRepoInterface) :
    BaseUseCase<Task<Void>, String>() {
    override suspend fun run(params: String): Result<Task<Void>, Exception> {
        return try {
            Result.Success(databaseAuthRepoInterface.sendPasswordResetRequest(params))
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}