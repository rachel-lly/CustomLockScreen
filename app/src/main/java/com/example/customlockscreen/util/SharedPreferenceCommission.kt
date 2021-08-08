package com.example.customlockscreen.util

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPreferenceCommission<T>(
        val context: Context,
        val name:String,
        val defaultValue: T
): ReadWriteProperty<Any?, T>{

    val sharedPreference: SharedPreferences by lazy {
        context.getSharedPreferences("LABEL_EVENT",Context.MODE_PRIVATE)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name,value)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name,defaultValue)
    }

    private fun <T> findPreference(name: String,default: T): T = with(sharedPreference){

        val res: Any = when(default){
            is Long -> getLong(name,default)
            is Int -> getInt(name,default)
            is Boolean -> getBoolean(name,default)
            is Float -> getFloat(name,default)
            is String -> getString(name,default)
            else -> throw IllegalArgumentException("This type can't be Identified")
        }!!

        res as T
    }

    private fun <T> putPreference(name: String,value: T) = with(sharedPreference.edit()){
        when(value){
            is Long -> putLong(name,value)
            is Int -> putInt(name,value)
            is Boolean -> putBoolean(name,value)
            is Float -> putFloat(name,value)
            is String -> putString(name,value)
            else -> throw IllegalArgumentException("This type can't be saved")
        }.apply()
    }

}