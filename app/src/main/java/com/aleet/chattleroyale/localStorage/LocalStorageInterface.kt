package com.aleet.chattleroyale.localStorage

interface LocalStorageInterface {

    fun getUniqueUserId(): String?

    fun setUniqueUserId(uniqueId: String)

}