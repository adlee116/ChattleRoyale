package com.aleet.chattleroyale.useCases

import com.aleet.chattleroyale.utils.BaseUseCase
import com.google.android.gms.tasks.Task
import javax.inject.Inject

//class ConfirmPasswordResetUseCase @Inject constructor(val databaseAuthRepoInterface: DatabaseAuthRepoInterface) :
//    BaseUseCase<Task<String>, String>() {
//    override suspend fun run(params: String): Result<Task<String>, Exception> {
//        return try {
//            Result.Success(databaseAuthRepoInterface.verifyPasswordResetCode(params))
//        } catch (ex: Exception) {
//            Result.Failure(ex)
//        }
//    }
//}