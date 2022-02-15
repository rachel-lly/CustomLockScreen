package com.example.customlockscreen.application

import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV

class MyApplication:Application() {


    companion object{
        var _context:Application?=null

        fun getContext():Context{
            return _context!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        _context = this
    }
}