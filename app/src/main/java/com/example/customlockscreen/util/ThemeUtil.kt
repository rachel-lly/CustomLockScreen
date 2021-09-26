package com.example.customlockscreen.util

import android.content.Context
import android.content.res.Configuration

class ThemeUtil {

    companion object{
        //检查当前系统是否已开启暗黑模式
        fun getDarkModeStatus(context: Context): Boolean {
            val mode: Int = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return mode == Configuration.UI_MODE_NIGHT_YES
        }
    }
}