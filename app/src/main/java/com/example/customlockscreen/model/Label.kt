package com.example.customlockscreen.model


class Label(var text : String,var date: Long){

        fun getDay(): Long = (date-System.currentTimeMillis())/(1000*3600*24)

}


