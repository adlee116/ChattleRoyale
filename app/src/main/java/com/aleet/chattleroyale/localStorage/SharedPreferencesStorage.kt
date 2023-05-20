package com.aleet.chattleroyale.localStorage

import com.google.gson.Gson
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


}