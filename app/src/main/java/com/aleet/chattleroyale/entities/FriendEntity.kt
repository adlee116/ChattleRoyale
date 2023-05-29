package com.aleet.chattleroyale.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity @Parcelize
data class FriendEntity(
    @PrimaryKey val id: String,
    val status: Boolean
) :Parcelable

