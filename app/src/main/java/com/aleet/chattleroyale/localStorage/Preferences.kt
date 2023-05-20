package com.aleet.chattleroyale.localStorage

import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject

class Preferences @Inject constructor(
    prefs: SharedPreferences,
    gson: Gson
) : BasePreferences(prefs, gson) {

    companion object {
        const val FILE_NAME = "StorageDataRepository"
    }
}