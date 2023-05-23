package com.aleet.chattleroyale.useCases.user

import com.aleet.chattleroyale.models.User
import com.aleet.chattleroyale.repositories.FirebaseAuthRepo
import com.aleet.chattleroyale.utils.BaseUseCase
import com.aleet.chattleroyale.utils.Result
import javax.inject.Inject

class CreateInitialUserUseCase @Inject constructor(
    private val firebaseAuthRepo: FirebaseAuthRepo
) : BaseUseCase<Unit, User>() {
    override suspend fun run(params: User): Result<Unit, Exception> {
        return try {
            firebaseAuthRepo.createCRUser(params)
            Result.Success(Unit)
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}
