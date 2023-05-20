package com.aleet.chattleroyale.useCases

//class GetStoredBasicRoomUseCase @Inject constructor(
//    val localStorage: LocalStorageInterface
//) : EmptyParamsUseCase<Result<BasicRoom?, Exception>>() {
//    override suspend fun run(): Result<BasicRoom?, Exception> {
//        return try {
//            return Result.Success(localStorage.getRoom())
//        } catch (ex: Exception) {
//            Result.Failure(ex)
//        }
//    }
//}