package com.squadsandshots_android.useCases

import com.squadsandshots_android.core.utils.EmptyParamsUseCase
import com.squadsandshots_android.core.utils.Result
import com.squadsandshots_android.localStorage.BasicRoom
import com.squadsandshots_android.localStorage.LocalStorageInterface
import javax.inject.Inject

class GetStoredBasicRoomUseCase @Inject constructor(
    val localStorage: LocalStorageInterface
) : EmptyParamsUseCase<Result<BasicRoom?, Exception>>() {
    override suspend fun run(): Result<BasicRoom?, Exception> {
        return try {
            return Result.Success(localStorage.getRoom())
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}