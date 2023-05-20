package com.aleet.chattleroyale.models

import com.google.firebase.firestore.FieldValue

data class User(
    val uid: String,
    val userName: String?,
    val email: String?,
    val profilePicture: String?,
    val gamesWon: Int?,
    val gamesPlayed: Int?,
    val lastOnline: FieldValue,
    val friends: HashMap<Int, Boolean>
)


