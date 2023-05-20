package com.aleet.chattleroyale.useCases

//class JoinRoomUseCase @Inject constructor(
//    private val repository: RoomModelRepository
//    ) : BaseUseCase<Boolean, JoinModel>() {
//    override suspend fun run(params: JoinModel): Result<Boolean, Exception> {
//        return try {
//            repository.joinRoom(params.roomCode, params.player)
//            Result.Success(true)
//        } catch (ex: Exception) {
//            Result.Failure(ex)
//        }
//    }
//}