package com.aleet.chattleroyale.useCases.local.user

import com.aleet.chattleroyale.localStorage.UserDao
import com.aleet.chattleroyale.models.FriendEntity
import com.aleet.chattleroyale.models.User
import com.aleet.chattleroyale.models.UserEntity
import com.aleet.chattleroyale.utils.BaseUseCase
import com.aleet.chattleroyale.utils.Result
import javax.inject.Inject

class GetUserLocallyUseCase @Inject constructor(
    private val userDao: UserDao
) : BaseUseCase<User?, Int>() {
    override suspend fun run(params: Int): Result<User?, Exception> {
        return try {
            val userEntity = userDao.getUser(params)
            val user = userEntity.toUser()
            Result.Success(user)
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}

fun UserEntity.toUser(): User {
    return User(
        uid = this.uid,
        userName = this.userName,
        email = this.email,
        profilePicture = this.profilePicture,
        gamesWon = this.gamesWon,
        gamesPlayed = this.gamesPlayed,
        lastOnline = this.lastOnline,
        friends = friendConverter(this.friends)
    )
}

fun friendConverter(friends: List<FriendEntity>?): HashMap<String, Boolean> {
    val friendHash = HashMap<String, Boolean>()
    friends?.forEach {
        friendHash[it.id] = it.status
    }
    return friendHash
}