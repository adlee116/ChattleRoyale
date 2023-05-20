package com.squadsandshots_android.localStorage

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import javax.inject.Inject

open class BasePreferences @Inject constructor(
    private val prefs: SharedPreferences,
    private val gson: Gson
) {

    fun getString(key: String, defValue: String = "") = prefs.getString(key, defValue) ?: ""
    fun getNullableString(key: String, defValue: String? = null) = prefs.getString(key, defValue)

    fun getInt(key: String, defValue: Int = 0) = prefs.getInt(key, defValue)
    fun getBoolean(key: String, defValue: Boolean = false) = prefs.getBoolean(key, defValue)

    fun contains(key: String): Boolean = prefs.contains(key)

    open fun delete() = prefs.edit(commit = true) { clear() }

    fun delete(key: String) {
        prefs.edit(commit = true) { remove(key) }
    }

    fun insert(key: String, value: Any?) {
        prefs.edit(commit = true) {
            when (value) {
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Int -> putInt(key, value)
                else -> putString(key, gson.toJson(value))
            }
        }
    }

}
