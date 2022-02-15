package com.example.customlockscreen.util

import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class KvCommission<T>(
    private val key: String,
    private val defaultValue: T
) : ReadWriteProperty<Any?,T>{

    private val mmkv by lazy{
        MMKV.defaultMMKV()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return when(defaultValue){
            is String -> mmkv.decodeString(key,defaultValue)
            is Float -> mmkv.decodeFloat(key,defaultValue)
            is Boolean -> mmkv.decodeBool(key,defaultValue)
            is Int -> mmkv.decodeInt(key,defaultValue)
            is Long -> mmkv.decodeLong(key,defaultValue)
            is Double -> mmkv.decodeDouble(key,defaultValue)
            is ByteArray -> mmkv.decodeBytes(key,defaultValue)
            else -> throw  IllegalArgumentException("unsupport type")
        } as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        mmkv.let {
            when(value){
                is String -> it.encode(key,value)
                is Float -> it.encode(key,value)
                is Boolean -> it.encode(key,value)
                is Int -> it.encode(key,value)
                is Long -> it.encode(key,value)
                is Double -> it.encode(key,value)
                is ByteArray -> it.encode(key,value)
                else -> return@let
            }
        }
    }

    fun removeKey() = mmkv.removeValueForKey(key)

    fun cleanAllMMKV() = mmkv.clearAll()
}