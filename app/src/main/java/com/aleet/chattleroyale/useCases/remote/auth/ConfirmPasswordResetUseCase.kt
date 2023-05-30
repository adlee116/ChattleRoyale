package com.aleet.chattleroyale.useCases.remote.auth

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