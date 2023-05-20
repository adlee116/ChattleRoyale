package com.aleet.chattleroyale.useCases

import com.aleet.chattleroyale.utils.BaseUseCase
import com.google.firebase.database.ValueEventListener

import javax.inject.Inject

//class AddRoomListenerUseCase @Inject constructor(
//    private val roomModelRepo: RoomModelRepository
//    ) : BaseUseCase<ValueEventListener, ListenerRequest>() {
//    override suspend fun run(params: ListenerRequest): Result<ValueEventListener, Exception> {
//        return try {
//            Result.Success(roomModelRepo.createRoomListener(params))
//        } catch (ex: Exception) {
//            Result.Failure(ex)
//        }
//    }
//}