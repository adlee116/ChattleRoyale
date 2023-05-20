package com.squadsandshots_android.useCases

import com.squadsandshots_android.core.utils.BaseUseCase
import com.squadsandshots_android.core.utils.Result
import com.squadsandshots_android.firebaseModels.RoomModel
import com.squadsandshots_android.repositories.RoomModelRepository
import javax.inject.Inject

class CreateRoomUseCase @Inject constructor(
    private val roomModelRepository: RoomModelRepository
): BaseUseCase<Unit, RoomModel>() {
    override suspend fun run(params: RoomModel): Result<Unit, Exception> {
        return Result.Success(roomModelRepository.createRoom(params))
    }
}