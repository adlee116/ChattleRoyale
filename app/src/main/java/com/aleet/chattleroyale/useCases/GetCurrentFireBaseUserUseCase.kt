package com.aleet.chattleroyale.useCases

//class GetCurrentFireBaseUserUseCase @Inject constructor(
//    private val databaseAuthRepoInterface: DatabaseAuthRepoInterface
//): EmptyParamsUseCase<Result<FirebaseUser?, Exception>>() {
//
//    override suspend fun run(): Result<FirebaseUser?, Exception> {
//        return try {
//            Result.Success(databaseAuthRepoInterface.getCurrentUser())
//        } catch (ex: Exception) {
//            Result.Failure(ex)
//        }
//    }
//}