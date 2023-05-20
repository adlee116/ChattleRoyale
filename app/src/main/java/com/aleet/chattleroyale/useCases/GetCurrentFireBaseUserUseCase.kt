package com.squadsandshots_android.useCases

import com.google.firebase.auth.FirebaseUser
import com.squadsandshots_android.core.utils.EmptyParamsUseCase
import com.squadsandshots_android.core.utils.Result
import com.squadsandshots_android.repositories.DatabaseAuthRepoInterface
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