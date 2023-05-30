package com.aleet.chattleroyale.localStorage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aleet.chattleroyale.entities.FriendEntity
import com.aleet.chattleroyale.entities.UserEntity
import com.aleet.chattleroyale.localStorage.ChattleRoyaleDatabase.Companion.VERSION

@Database(
    entities = [UserEntity::class, FriendEntity::class],
    version = VERSION,
    exportSchema = false
)

abstract class ChattleRoyaleDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun friendDao(): FriendDao

    companion object {
        const val VERSION = 1
        const val NAME = "CHATTLE_ROYALE_DATABASE"
    }
}
