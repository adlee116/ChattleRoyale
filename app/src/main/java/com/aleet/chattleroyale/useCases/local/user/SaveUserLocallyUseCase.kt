package com.aleet.chattleroyale.useCases.local.user

import com.aleet.chattleroyale.localStorage.UserDao
import com.aleet.chattleroyale.models.User
import com.aleet.chattleroyale.utils.BaseUseCase
import com.aleet.chattleroyale.utils.Result
import javax.inject.Inject

class SaveUserLocallyUseCase @Inject constructor(private val userDao: UserDao): BaseUseCase<Unit, User>() {
    override suspend fun run(params: User): Result<Unit, Exception> {
        return try {
            userDao.insert(params)
            Result.Success(Unit)
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}