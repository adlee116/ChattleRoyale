package com.aleet.chattleroyale.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class User(
    val uid: String,
    var userName: String?,
    var email: String?,
    var profilePicture: String?,
    var gamesWon: Int?,
    var gamesPlayed: Int?,
    var lastOnline: Long?,
    var friends: HashMap<String, Boolean>?
)

fun User.toUserEntity(): UserEntity {
    val friendList = friends?.map { FriendEntity(it.key, it.value) }
    return UserEntity(uid, userName, email, profilePicture, gamesWon, gamesPlayed, lastOnline, friendList)
}

@Entity
data class UserEntity(
    @PrimaryKey val uid: String,
    var userName: String?,
    var email: String?,
    var profilePicture: String?,
    var gamesWon: Int?,
    var gamesPlayed: Int?,
    var lastOnline: Long?,
    var friends: List<FriendEntity>?
)

@Entity
data class FriendEntity(
    @PrimaryKey val id: String,
    val status: Boolean
)



