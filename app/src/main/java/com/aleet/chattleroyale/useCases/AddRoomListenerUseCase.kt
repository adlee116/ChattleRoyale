package com.squadsandshots_android.useCases

import com.google.firebase.database.ValueEventListener
import com.squadsandshots_android.core.utils.BaseUseCase
import com.squadsandshots_android.core.utils.Result
import com.squadsandshots_android.firebaseModels.ListenerRequest
import com.squadsandshots_android.repositories.RoomModelRepository
import javax.inject.Inject

class AddRoomListenerUseCase @Inject constructor(
    private val roomModelRepo: RoomModelRepository
    ) : BaseUseCase<ValueEventListener, ListenerRequest>() {
    override suspend fun run(params: ListenerRequest): Result<ValueEventListener, Exception> {
        return try {
            Result.Success(roomModelRepo.createRoomListener(params))
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}