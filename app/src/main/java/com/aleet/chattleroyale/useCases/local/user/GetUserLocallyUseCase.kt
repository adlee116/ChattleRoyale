package com.aleet.chattleroyale.useCases.local.user

import com.aleet.chattleroyale.entities.FriendEntity
import com.aleet.chattleroyale.entities.UserEntity
import com.aleet.chattleroyale.models.User
import com.aleet.chattleroyale.repositories.localStorage.FriendsRepository
import com.aleet.chattleroyale.repositories.localStorage.UsersRepository
import com.aleet.chattleroyale.utils.BaseUseCase
import com.aleet.chattleroyale.utils.Result
import javax.inject.Inject

class GetUserLocallyUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
    private val friendsRepository: FriendsRepository
) : BaseUseCase<User?, String>() {
    override suspend fun run(params: String): Result<User?, Exception> {
        return try {
            val userEntity = usersRepository.getUser(params)
            val friends = friendsRepository.getFriends()
            val user = userEntity.toUser(friends)
            Result.Success(user)
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}

fun UserEntity.toUser(friends: List<FriendEntity>): User {
    val entity = this
    return User().apply {
        uid = entity.uid
        userName = entity.userName ?: ""
        email = entity.email ?: ""
        profilePicture = entity.profilePicture ?: ""
        gamesWon = entity.gamesWon ?: 0
        gamesPlayed = entity.gamesPlayed ?: 0
        lastOnline = System.currentTimeMillis()
        this.friends = friendConverter(friends)
    }
}

fun friendConverter(friends: List<FriendEntity>?): HashMap<String, Boolean> {
    val friendHash = HashMap<String, Boolean>()
    friends?.forEach {
        friendHash[it.id] = it.status
    }
    return friendHash
}