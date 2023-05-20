package com.aleet.chattleroyale.repositories

import com.google.firebase.database.DatabaseReference

interface DatabaseRepositoryInterface {
    fun getReference(path: String) : DatabaseReference

    fun postToDatabase(path: String, id: String, postValue: Any)

}
