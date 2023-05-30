package com.aleet.chattleroyale.repositories.localStorage

import com.aleet.chattleroyale.entities.UserEntity
import com.aleet.chattleroyale.localStorage.ChattleRoyaleDatabase
import javax.inject.Inject

class UsersRepository @Inject constructor(private val db: ChattleRoyaleDatabase) {

    suspend fun getUser(id: String): UserEntity {
        return db.userDao().getUser(id)
    }

    suspend fun insertUser(user: UserEntity) {
        db.userDao().insertUser(user)
    }

    suspend fun deleteUser(user: UserEntity) {
        db.userDao().deleteUser(user)
    }
}