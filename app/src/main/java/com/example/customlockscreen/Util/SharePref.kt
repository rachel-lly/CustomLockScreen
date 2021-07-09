package com.example.customlockscreen.Util

import android.content.Context


import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharePref<T>(
        private val context: Context,
        private val key :String,
        private val defaultValue: T,
        private val prefName : String = "LABEL_EVENT",

) : ReadWriteProperty<Any?,T>{



    val preferences by lazy { context.getSharedPreferences(prefName, Context.MODE_PRIVATE) }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(key,defaultValue)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(key,defaultValue)
    }

    private fun <T> findPreference(name: String, default: T): T = with(preferences) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("Unsupported type")
        }!!

        res as T
    }

    private fun <T> putPreference(name: String, value: T) = with(preferences.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("Unsupported type")
        }.apply()
    }

}