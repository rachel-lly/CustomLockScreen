package com.example.customlockscreen.util

import android.content.Context
import android.widget.Toast

class ToastUtil {

    companion object{
        fun Context.toast(message: String){
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }
    }
}