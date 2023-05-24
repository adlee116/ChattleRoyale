package com.aleet.chattleroyale.localStorage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aleet.chattleroyale.models.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM userentity WHERE uid = :userId LIMIT 1")
    fun getUser(userId: Int): UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("DELETE FROM userentity")
    suspend fun deleteAll()
}

//@Dao
//interface FriendDao {
//    @Query("SELECT * FROM friend WHERE id = :userId LIMIT 1")
//    fun getFriend(userId: Int): Flow<Friend>
//
//    @Query("SELECT * FROM friend")
//    fun getAllFriends(): List<Friend>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(friend: Friend)
//
//    @Query("DELETE FROM friend")
//    suspend fun deleteAll()
//}