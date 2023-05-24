package com.aleet.chattleroyale.repositories

import androidx.annotation.WorkerThread
import com.aleet.chattleroyale.localStorage.FriendDao
import com.aleet.chattleroyale.localStorage.UserDao
import com.aleet.chattleroyale.models.Friend
import com.aleet.chattleroyale.models.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    fun getUser(userId: Int): Flow<UserEntity> = userDao.getUser(userId)

    @WorkerThread
    suspend fun insert(user: UserEntity) {
        userDao.insert(user)
    }

    @WorkerThread
    suspend fun deleteAll() {
        userDao.deleteAll()
    }

}
class FriendRepository(private val friendDao: FriendDao) {

    fun getAllFriends(): List<Friend> = friendDao.getAllFriends()
    fun getFriend(userId: Int): Flow<Friend> = friendDao.getFriend(userId)

    @WorkerThread
    suspend fun insert(friend: Friend) {
        friendDao.insert(friend)
    }

    @WorkerThread
    suspend fun deleteAll() {
        friendDao.deleteAll()
    }

}
