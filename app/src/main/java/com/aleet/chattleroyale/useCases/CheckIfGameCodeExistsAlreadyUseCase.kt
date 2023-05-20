package com.squadsandshots_android.useCases

import com.google.firebase.database.ValueEventListener
import com.squadsandshots_android.core.utils.BaseUseCase
import com.squadsandshots_android.core.utils.Result
import com.squadsandshots_android.firebaseModels.ListenerRequest
import com.squadsandshots_android.repositories.RoomModelRepository
import javax.inject.Inject

class CreateRoomListenerUseCase @Inject constructor(
    private val roomRepository: RoomModelRepository
) : BaseUseCase<ValueEventListener, ListenerRequest>() {
    override suspend fun run(params: ListenerRequest): Result<ValueEventListener, Exception> {
        return Result.Success(roomRepository.createRoomListener(params))
    }
}