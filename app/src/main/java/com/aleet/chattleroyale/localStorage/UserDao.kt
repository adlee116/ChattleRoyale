package com.aleet.chattleroyale.localStorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aleet.chattleroyale.entities.FriendEntity
import com.aleet.chattleroyale.entities.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user_entity WHERE uid LIKE :userId LIMIT 1")
    suspend fun getUser(userId: String): UserEntity

    @Query("SELECT * FROM user_entity")
    suspend fun getAllUsers(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Delete
    suspend fun deleteUser(user: UserEntity)
}

@Dao
interface FriendDao {

    @Query("SELECT * FROM friendEntity")
    suspend fun getAllFriends(): List<FriendEntity>

    @Query("DELETE FROM friendentity")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(friends: List<FriendEntity>)

}