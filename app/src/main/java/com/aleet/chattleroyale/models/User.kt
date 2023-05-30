package com.aleet.chattleroyale.models

import android.os.Parcelable
import com.aleet.chattleroyale.entities.FriendEntity
import com.aleet.chattleroyale.entities.UserEntity
import kotlinx.parcelize.Parcelize

@Parcelize
class User: Parcelable {
    var uid: String = ""
    var userName: String = ""
    var email: String = ""
    var profilePicture: String  = ""
    var gamesWon: Int = 0
    var gamesPlayed: Int = 0
    var lastOnline: Long = 0
    var friends: HashMap<String, Boolean> = hashMapOf()
}

fun User.toUserEntityAndFriends(): Pair<UserEntity, List<FriendEntity>?> {
    val friendList = friends.map { FriendEntity(it.key, it.value) }
    val user = UserEntity(uid = uid, userName = userName, email = email, profilePicture = profilePicture, gamesWon = gamesWon, gamesPlayed = gamesPlayed, lastOnline = lastOnline)
    return Pair(user, friendList)
}

fun UserEntity.toUser(friendList: List<FriendEntity>): User {
    val entity = this
    val friends = HashMap<String, Boolean>()
    friendList.forEach { friend ->
        friends[friend.id] = friend.status
    }
    return User().apply {
        uid = entity.uid
        userName = entity.userName ?: ""
        email = entity.email ?: ""
        profilePicture = entity.profilePicture ?: ""
        gamesWon = entity.gamesWon ?: 0
        gamesPlayed = entity.gamesPlayed ?: 0
        lastOnline = System.currentTimeMillis()
        this.friends = friends
    }
}

