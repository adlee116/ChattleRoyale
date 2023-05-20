package com.squadsandshots_android.useCases

import com.squadsandshots_android.core.utils.BaseUseCase
import com.squadsandshots_android.core.utils.Result
import com.squadsandshots_android.repositories.RoomModelRepository
import com.squadsandshots_android.requestModels.JoinModel
import javax.inject.Inject

class JoinRoomUseCase @Inject constructor(
    private val repository: RoomModelRepository
    ) : BaseUseCase<Boolean, JoinModel>() {
    override suspend fun run(params: JoinModel): Result<Boolean, Exception> {
        return try {
            repository.joinRoom(params.roomCode, params.player)
            Result.Success(true)
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}