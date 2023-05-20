package com.squadsandshots_android.repositories

import com.google.firebase.database.DatabaseReference

interface DatabaseRepositoryInterface {
    fun getReference(path: String) : DatabaseReference

    fun postToDatabase(path: String, id: String, postValue: Any)

    companion object {
        val ROOM = "room"
        val PLAYERS = "players"
        val RULES = "rules"
        val DRINKS = "drinks"
    }
}
