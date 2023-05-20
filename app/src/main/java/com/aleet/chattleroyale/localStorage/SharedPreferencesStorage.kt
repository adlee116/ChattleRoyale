package com.squadsandshots_android.localStorage

import com.google.gson.Gson
import com.squadsandshots_android.firebaseModels.Player
import javax.inject.Inject

class SharedPreferencesStorage @Inject constructor(
    private val preferences: BasePreferences,
    private val gson: Gson
    ): LocalStorageInterface {

    override fun getUniqueUserId(): String? {
        return preferences.getNullableString(LocalStorageRepository.UNIQUE_USER_ID)
    }

    override fun setUniqueUserId(uniqueId: String) {
        preferences.insert(LocalStorageRepository.UNIQUE_USER_ID, uniqueId)
    }

    override fun setPlayer(player: Player) {
        preferences.insert(LocalStorageRepository.PLAYER, player)
    }

    override fun getPlayer(): Player? {
        val playerString = preferences.getString(LocalStorageRepository.PLAYER)
        return gson.fromJson(playerString, Player::class.java)
    }

    override fun getRoom(): BasicRoom? {
        val roomString = preferences.getString((LocalStorageRepository.ROOM))
        return gson.fromJson(roomString, BasicRoom::class.java)
    }

    override fun setRoom(room: BasicRoom) {
        preferences.insert(LocalStorageRepository.ROOM, room)
    }


}