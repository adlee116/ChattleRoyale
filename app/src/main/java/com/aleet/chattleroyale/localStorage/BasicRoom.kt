package com.squadsandshots_android.localStorage

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class BasicRoom(
    @PrimaryKey
    val roomCode: String
    ): Parcelable