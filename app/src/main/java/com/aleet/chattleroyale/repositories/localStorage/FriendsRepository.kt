package com.aleet.chattleroyale.repositories.localStorage

import com.aleet.chattleroyale.entities.FriendEntity
import com.aleet.chattleroyale.localStorage.ChattleRoyaleDatabase
import javax.inject.Inject

class FriendsRepository @Inject constructor(private val db: ChattleRoyaleDatabase) {

    suspend fun getFriends(): List<FriendEntity> {
        return db.friendDao().getAllFriends()
    }

    suspend fun updateFriends(friends: List<FriendEntity>) {
        db.friendDao().insertAll(friends)
    }

    suspend fun deleteAllFriends() {
        db.friendDao().deleteAll()
    }
}