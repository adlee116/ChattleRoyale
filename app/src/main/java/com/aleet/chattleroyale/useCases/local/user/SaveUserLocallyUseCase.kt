package com.aleet.chattleroyale.useCases.local.user

import com.aleet.chattleroyale.models.User
import com.aleet.chattleroyale.models.toUserEntityAndFriends
import com.aleet.chattleroyale.repositories.localStorage.FriendsRepository
import com.aleet.chattleroyale.repositories.localStorage.UsersRepository
import com.aleet.chattleroyale.utils.BaseUseCase
import com.aleet.chattleroyale.utils.Result
import javax.inject.Inject

class SaveUserLocallyUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
    private val friendsRepository: FriendsRepository
): BaseUseCase<Unit, User>() {
    override suspend fun run(params: User): Result<Unit, Exception> {
        return try {
            val userAndFriends = params.toUserEntityAndFriends()
            usersRepository.insertUser(userAndFriends.first)
            friendsRepository.deleteAllFriends()
            friendsRepository.updateFriends(userAndFriends.second ?: listOf())
            Result.Success(Unit)
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}