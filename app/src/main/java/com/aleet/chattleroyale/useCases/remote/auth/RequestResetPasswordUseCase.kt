package com.aleet.chattleroyale.useCases.remoteAuth

//class RequestResetPasswordUseCase @Inject constructor(val databaseAuthRepoInterface: DatabaseAuthRepoInterface) :
//    BaseUseCase<Task<Void>, String>() {
//    override suspend fun run(params: String): Result<Task<Void>, Exception> {
//        return try {
//            Result.Success(databaseAuthRepoInterface.sendPasswordResetRequest(params))
//        } catch (ex: Exception) {
//            Result.Failure(ex)
//        }
//    }
//}