package com.squadsandshots_android.localStorage

import com.squadsandshots_android.firebaseModels.Player

interface LocalStorageInterface {

    fun getUniqueUserId(): String?

    fun setUniqueUserId(uniqueId: String)

    fun setPlayer(player: Player)

    fun getPlayer(): Player?

    fun getRoom(): BasicRoom?

    fun setRoom(room: BasicRoom)
}