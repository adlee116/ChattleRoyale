package com.aleet.chattleroyale.useCases.local.user

import com.aleet.chattleroyale.localStorage.LocalStorageInterface
import com.aleet.chattleroyale.utils.BaseUseCase
import com.aleet.chattleroyale.utils.Result

class GetUniqueUserIdUseCase(val localStorageRepository: LocalStorageInterface
): BaseUseCase<String?, Unit>() {
    override suspend fun run(params: Unit): Result<String?, Exception> {
        return try {
            Result.Success(localStorageRepository.getUniqueUserId())
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}