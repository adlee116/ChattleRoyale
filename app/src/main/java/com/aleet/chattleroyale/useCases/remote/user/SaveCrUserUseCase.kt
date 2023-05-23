package com.aleet.chattleroyale.useCases.user

import com.aleet.chattleroyale.models.User
import com.aleet.chattleroyale.repositories.UserRemoteRepo
import com.aleet.chattleroyale.utils.BaseUseCase
import com.aleet.chattleroyale.utils.Result
import javax.inject.Inject

class SaveCrUserUseCase @Inject constructor(
    private val userRemoteRepo: UserRemoteRepo
): BaseUseCase<Unit, User>() {
    override suspend fun run(params: User): Result<Unit, Exception> {
        return try {
            userRemoteRepo.saveUser(params)
            Result.Success(Unit)
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}