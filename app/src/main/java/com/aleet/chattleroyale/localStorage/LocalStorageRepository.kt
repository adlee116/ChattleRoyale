package com.aleet.chattleroyale.localStorage

import java.util.UUID
import javax.inject.Inject

class LocalStorageRepository @Inject constructor(private val storageLocation: LocalStorageInterface) {

    fun getOrCreateUserId(): String {
        val id = getUniqueUserId()
        return id ?: run {
            val newCode = generateUniqueUserCode()
            setUniqueUserId(newCode)
            newCode
        }
    }

    fun getUniqueUserId(): String? {
        return storageLocation.getUniqueUserId()
    }

    fun setUniqueUserId(uniqueId: String) {
        storageLocation.setUniqueUserId(uniqueId)
    }

    fun generateUniqueUserCode(): String {
        return UUID.randomUUID().toString()
    }

    companion object {
        const val UNIQUE_USER_ID = "uniqueUserId"
        const val PLAYER = "player"
        const val ROOM = "room"
    }
}