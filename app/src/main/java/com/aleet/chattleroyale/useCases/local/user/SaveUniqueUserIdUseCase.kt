package com.aleet.chattleroyale.useCases.local.user

import com.aleet.chattleroyale.localStorage.LocalStorageInterface
import com.aleet.chattleroyale.utils.BaseUseCase
import com.aleet.chattleroyale.utils.Result
import javax.inject.Inject

class SaveUniqueUserIdUseCase @Inject constructor(private val localStorageInterface: LocalStorageInterface): BaseUseCase<Unit, String>() {
    override suspend fun run(params: String): Result<Unit, Exception> {
        return try {
            Result.Success(localStorageInterface.setUniqueUserId(params))
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}