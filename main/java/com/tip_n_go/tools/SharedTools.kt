package com.tip_n_go.tools

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.tip_n_go.App

object SharedTools {

    fun sharedPrefs(): SharedPreferences =
        App.application.getSharedPreferences(App.application.packageName, Context.MODE_PRIVATE)

    fun removeJwtToken() {
        sharedPrefs().edit()
            .remove(TOKEN)
            .apply()
    }

    private fun putAsString(key: String, value: Any?) {
        val valueAsString = Gson().toJson(value)
        sharedPrefs().edit()
            .putString(key, valueAsString)
            .apply()
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> putAsString(key, value)
        }
    }

    // if object is not primitive or wrapper u should specify type
    // todo REDO
    inline operator fun <reified T : Any> SharedPreferences.get(
        key: String,
        defaultValue: T? = null
    ): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> {
                val valueAsString = sharedPrefs().getString(key, "")
                return Gson().fromJson(valueAsString, defaultValue!!::class.java)
            }
        }
    }
}
