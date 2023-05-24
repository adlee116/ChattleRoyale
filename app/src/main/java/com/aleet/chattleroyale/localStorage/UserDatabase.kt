package com.aleet.chattleroyale.localStorage


import androidx.room.Database
import androidx.room.RoomDatabase
import com.aleet.chattleroyale.models.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract val userDao: UserDao
}