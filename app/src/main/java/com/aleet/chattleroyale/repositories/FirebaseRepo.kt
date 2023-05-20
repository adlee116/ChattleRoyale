package com.aleet.chattleroyale.repositories

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseRepo @Inject constructor(): DatabaseRepositoryInterface {
    private val databaseReference = Firebase.database

    override fun getReference(path: String) : DatabaseReference {
        return databaseReference.getReference(path)
    }

    override fun postToDatabase(path: String, id: String, postValue: Any) {
        val reference = getReference(path)
        reference.child(path).child(id).setValue(postValue)
    }
}