package com.aleet.chattleroyale.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user_entity")
@Parcelize
data class UserEntity(
    @PrimaryKey var uid: String,
    @ColumnInfo("user_name") var userName: String?,
    @ColumnInfo("email") var email: String?,
    @ColumnInfo("profile_picture") var profilePicture: String?,
    @ColumnInfo("games_won") var gamesWon: Int?,
    @ColumnInfo("games_played") var gamesPlayed: Int?,
    @ColumnInfo("last_online") var lastOnline: Long?
): Parcelable